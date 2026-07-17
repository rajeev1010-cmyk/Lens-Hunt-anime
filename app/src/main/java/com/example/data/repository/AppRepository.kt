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
import kotlin.math.max

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
        
        // ISSUE 8: Database Validation (Log missing metadata)
        characters.forEach { char ->
            val isMissing = char.designer.isBlank() || char.designer == "Unknown" ||
                char.designLanguage.isBlank() || char.designLanguage == "Design" ||
                char.designPrinciples.isBlank() || char.designPrinciples == "Unknown" ||
                char.visualTraits.isBlank() || char.visualTraits == "Trait" ||
                char.designBreakdown.isBlank() || char.designBreakdown == "Unknown" ||
                char.description.isBlank() || char.description == "Desc"
                
            if (isMissing) {
                Log.w("DIAGNOSTIC", "Missing metadata for character: ${char.name} (${char.id})")
            }
        }

        val (primaryCluster, confidence) = ClusterManager.determineUserCluster(axes)
        
        var targetClusters = listOf(primaryCluster)
        if (confidence < 0.7f) {
            val nearest = ClusterManager.getNearestClusters(primaryCluster)
            targetClusters = targetClusters + nearest
        }
        
        val filteredCharacters = characters.filter { it.cluster in targetClusters }
        val candidates = if (filteredCharacters.isNotEmpty()) filteredCharacters else characters

        // Calculate raw distances
        val rawResults = candidates.map { character ->
            val (_, distance, contributions) = SimilarityCalculator.calculateWithDetails(axes.toArray(), character.profile.toArray())
            Triple(character, distance, contributions)
        }

        val minDist = rawResults.minOfOrNull { it.second } ?: 0f
        val maxDist = rawResults.maxOfOrNull { it.second } ?: 1f
        val range = max(0.0001f, maxDist - minDist)

        var results = rawResults.map { (character, distance, contributions) ->
            var normalizedScore = 0.98f - 0.28f * ((distance - minDist) / range)
            
            if (character.cluster == primaryCluster) {
                normalizedScore += 0.02f
            } else if (confidence < 0.7f && character.cluster in targetClusters) {
                normalizedScore -= 0.03f
            } else {
                normalizedScore -= 0.15f
            }

            normalizedScore = normalizedScore.coerceIn(0f, 0.98f)
            MatchResult(character, normalizedScore, 0, distance, contributions)
        }.sortedByDescending { it.score }
        
        // ISSUE 4 & 5: Fix Similarity Scores & Improve Diversity
        val diverseResults = mutableListOf<MatchResult>()
        var rank = 0
        for (result in results) {
            // Natural decay so they aren't all 95%+
            var penalty = rank * 0.04f 
            
            // Diversity penalty if too similar to already accepted characters
            for (accepted in diverseResults) {
                val distanceBetweenThem = SimilarityCalculator.calculateWithDetails(
                    result.character.profile.toArray(),
                    accepted.character.profile.toArray()
                ).second
                
                if (distanceBetweenThem < 0.3f) {
                    penalty += 0.06f
                }
            }
            
            val finalScore = (result.score - penalty).coerceIn(0.50f, 0.98f)
            val percentage = (finalScore * 100).toInt().coerceIn(0, 100)
            diverseResults.add(MatchResult(result.character, finalScore, percentage, result.distance, result.contributions))
            rank++
        }

        return diverseResults.sortedByDescending { it.score }.take(5)
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
