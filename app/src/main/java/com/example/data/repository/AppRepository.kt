package com.example.data.repository

import android.util.Log
import com.example.data.local.AppDao
import com.example.data.local.CharacterDataSeed
import com.example.data.local.CharacterEntity
import com.example.data.local.HistoryEntity
import com.example.data.local.SettingsEntity
import com.example.data.model.VisualAxes
import com.example.domain.matcher.ClusterManager
import com.example.domain.matcher.MatchResult
import com.example.domain.matcher.SimilarityCalculator
import kotlinx.coroutines.flow.Flow

class AppRepository(private val dao: AppDao) {
    val allCharacters: Flow<List<CharacterEntity>> = dao.getAllCharactersFlow()
    val history: Flow<List<HistoryEntity>> = dao.getHistoryFlow()
    val settings: Flow<SettingsEntity?> = dao.getSettingsFlow()

    suspend fun seedDatabaseIfEmpty() {
        val current = dao.getAllCharacters()
        if (current.isEmpty()) {
            dao.insertCharacters(CharacterDataSeed.characters)
        }
    }

    suspend fun findMatches(axes: VisualAxes): List<MatchResult> {
        val characters = dao.getAllCharacters()
        if (characters.isEmpty()) return emptyList()
        
        val (primaryCluster, confidence) = ClusterManager.determineUserCluster(axes)
        
        var targetClusters = listOf(primaryCluster)
        if (confidence < 0.7f) {
            val nearest = ClusterManager.getNearestClusters(primaryCluster)
            targetClusters = targetClusters + nearest
        }
        
        val filteredCharacters = characters.filter { it.cluster in targetClusters }
        // fallback if none matches
        val candidates = if (filteredCharacters.isNotEmpty()) filteredCharacters else characters

        val results = candidates.map { character ->
            val (baseScore, distance, contributions) = SimilarityCalculator.calculateWithDetails(axes.toArray(), character.profile.toArray())
            
            // Apply cluster weighting if confidence is low
            var finalScore = baseScore
            if (confidence < 0.7f) {
                if (character.cluster == primaryCluster) {
                    finalScore *= 1.0f // Give it full weight, wait... The prompt says Primary 70%, Neighbour 30%.
                    // Actually, if we just multiply by 0.7 vs 0.3
                }
                // But the prompt says: Weight: Primary Cluster 70%, Neighbour Cluster 30%.
                // This could mean we scale the similarity score based on which cluster it's from.
                val clusterWeight = if (character.cluster == primaryCluster) 0.7f else 0.3f
                finalScore = finalScore * 0.5f + (finalScore * clusterWeight) // Just to give it a boost relative to others.
                // Or simply: finalScore = baseScore * clusterWeight. Let's do a milder boost.
                finalScore = baseScore * clusterWeight * 2f // if 0.7 -> 1.4x, if 0.3 -> 0.6x
                finalScore = finalScore.coerceIn(0f, 1f)
            }
            
            val percentage = (finalScore * 100).toInt().coerceIn(0, 100)
            MatchResult(character, finalScore, percentage, distance, contributions)
        }.sortedByDescending { it.score }

        return results.take(5)
    }

    suspend fun saveHistory(matchedCharacterId: String, score: Float, userProfile: VisualAxes) {
        val historyEntity = HistoryEntity(
            timestamp = System.currentTimeMillis(),
            matchedCharacterId = matchedCharacterId,
            similarityScore = score,
            userProfile = userProfile
        )
        dao.insertHistory(historyEntity)
    }

    suspend fun updateSettings(settingsEntity: SettingsEntity) {
        dao.updateSettings(settingsEntity)
    }
}
