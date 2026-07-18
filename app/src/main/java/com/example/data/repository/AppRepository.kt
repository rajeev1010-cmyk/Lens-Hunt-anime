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

    private fun stylize(humanValue: Float, exaggeration: Float): Float {
        val centered = humanValue - 0.5f
        val pushed = centered * (1f + exaggeration)
        return (pushed + 0.5f).coerceIn(0.02f, 0.98f)
    }

    private fun stylizeAxes(axes: VisualAxes): VisualAxes {
        val exaggerationByField = mapOf(
            "eyeSizeRatio" to 0.9f, "eyeRoundness" to 0.7f, "eyeTilt" to 0.5f,
            "jawWidthRatio" to 0.6f, "chinSharpness" to 0.6f, "faceShape" to 0.5f,
            "noseWidthRatio" to 0.2f, "noseLengthRatio" to 0.2f, "earSizeRatio" to 0.15f
        )
        fun ex(field: String): Float = exaggerationByField[field] ?: 0.35f
        
        return axes.copy(
            faceShape = stylize(axes.faceShape, ex("faceShape")),
            jawWidthRatio = stylize(axes.jawWidthRatio, ex("jawWidthRatio")),
            jawAngle = stylize(axes.jawAngle, ex("jawAngle")),
            chinLengthRatio = stylize(axes.chinLengthRatio, ex("chinLengthRatio")),
            chinSharpness = stylize(axes.chinSharpness, ex("chinSharpness")),
            foreheadWidthRatio = stylize(axes.foreheadWidthRatio, ex("foreheadWidthRatio")),
            foreheadHeightRatio = stylize(axes.foreheadHeightRatio, ex("foreheadHeightRatio")),
            cheekboneWidthRatio = stylize(axes.cheekboneWidthRatio, ex("cheekboneWidthRatio")),
            faceHeightRatio = stylize(axes.faceHeightRatio, ex("faceHeightRatio")),
            eyeSizeRatio = stylize(axes.eyeSizeRatio, ex("eyeSizeRatio")),
            eyeSpacingRatio = stylize(axes.eyeSpacingRatio, ex("eyeSpacingRatio")),
            eyeTilt = stylize(axes.eyeTilt, ex("eyeTilt")),
            eyeRoundness = stylize(axes.eyeRoundness, ex("eyeRoundness")),
            eyebrowThickness = stylize(axes.eyebrowThickness, ex("eyebrowThickness")),
            eyebrowCurve = stylize(axes.eyebrowCurve, ex("eyebrowCurve")),
            noseLengthRatio = stylize(axes.noseLengthRatio, ex("noseLengthRatio")),
            noseWidthRatio = stylize(axes.noseWidthRatio, ex("noseWidthRatio")),
            mouthWidthRatio = stylize(axes.mouthWidthRatio, ex("mouthWidthRatio")),
            lipThickness = stylize(axes.lipThickness, ex("lipThickness")),
            earSizeRatio = stylize(axes.earSizeRatio, ex("earSizeRatio")),
            hairlineHeight = stylize(axes.hairlineHeight, ex("hairlineHeight")),
            neckWidthRatio = stylize(axes.neckWidthRatio, ex("neckWidthRatio")),
            symmetry = axes.symmetry, 
            expressionNeutrality = axes.expressionNeutrality,
            stylizationIndex = axes.stylizationIndex
        )
    }

    val allCharacters: Flow<List<CharacterEntity>> = dao.getAllCharactersFlow()
    val history: Flow<List<HistoryEntity>> = dao.getHistoryFlow()
    val settings: Flow<SettingsEntity?> = dao.getSettingsFlow()

    suspend fun seedDatabaseIfEmpty() {
        val current = dao.getAllCharacters()
        if (current.isEmpty()) {
            dao.insertCharacters(CharacterDataSeed.characters)
        }
    }

    suspend fun findMatches(
        axes: VisualAxes,
        visualPresentation: String,
        presentationConfidence: Float,
        genderFilter: String = "AUTO"
    ): Pair<List<MatchResult>, com.example.domain.matcher.MatchDebugInfo> {
        val characters = dao.getAllCharacters()
        if (characters.isEmpty()) return Pair(emptyList(), com.example.domain.matcher.MatchDebugInfo())
        
        // Step 2 - Filter Character Database
        val candidatesByPresentation = when (genderFilter.uppercase()) {
            "MALE" -> characters.filter { it.gender == "male" || it.gender == "nonbinary" }
            "FEMALE" -> characters.filter { it.gender == "female" || it.gender == "nonbinary" }
            "ANY" -> characters
            else -> {
                if (presentationConfidence >= 0.6f) {
                    when (visualPresentation) {
                        "male" -> characters.filter { it.gender == "male" || it.gender == "nonbinary" }
                        "female" -> characters.filter { it.gender == "female" || it.gender == "nonbinary" }
                        else -> characters
                    }
                } else {
                    characters
                }
            }
        }
        
        val debugInfo = com.example.domain.matcher.MatchDebugInfo(
            visualPresentation = visualPresentation,
            presentationConfidence = presentationConfidence,
            charactersLoaded = characters.size,
            charactersAfterFiltering = candidatesByPresentation.size
        )
        
        // Log Debug Info
        Log.d("MATCHING", "Detected Visual Presentation: $visualPresentation (conf: $presentationConfidence)")
        Log.d("MATCHING", "Characters Loaded: ${characters.size}")
        Log.d("MATCHING", "Characters After Filtering: ${candidatesByPresentation.size}")

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
        
        val filteredCharacters = candidatesByPresentation.filter { it.cluster in targetClusters }
        val candidates = if (filteredCharacters.isNotEmpty()) filteredCharacters else candidatesByPresentation

        // Calculate raw distances
        val stylizedAxes = stylizeAxes(axes)
        val rawResults = candidates.map { character ->
            val (_, distance, contributions) = SimilarityCalculator.calculateWithDetails(stylizedAxes.toArray(), character.profile.toArray())
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
            
            if (character.gender == "nonbinary" && visualPresentation != "unknown" && presentationConfidence >= 0.6f) {
                normalizedScore -= 0.03f
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

        return Pair(diverseResults.sortedByDescending { it.score }.take(10), debugInfo)
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
