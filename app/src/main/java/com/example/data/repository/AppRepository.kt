package com.example.data.repository

import android.util.Log
import com.example.data.local.AppDao
import com.example.data.local.CharacterDataSeed
import com.example.data.local.CharacterEntity
import com.example.data.local.HistoryEntity
import com.example.data.local.SettingsEntity
import com.example.data.model.VisualAxes
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

        Log.d("DIAGNOSTIC", "========== DIAGNOSTIC PIPELINE START ==========")
        Log.d("DIAGNOSTIC", "STEP 1 - VERIFY DATABASE")
        Log.d("DIAGNOSTIC", "Number of characters loaded: ${characters.size}")
        if (characters.size < 50) {
            Log.e("DIAGNOSTIC", "WARNING: Fewer than 50 characters loaded!")
        }
        var totalSize = 0
        characters.forEach { 
            Log.d("DIAGNOSTIC", "ID: ${it.id} | Name: ${it.name}")
            totalSize += it.toString().length
        }
        Log.d("DIAGNOSTIC", "Approx database size: $totalSize bytes")
        
        Log.d("DIAGNOSTIC", "STEP 2 - VERIFY FACE FEATURES")
        Log.d("DIAGNOSTIC", "Extracted vector: $axes")
        Log.d("DIAGNOSTIC", "faceLength: ${axes.faceLength}")
        Log.d("DIAGNOSTIC", "jawSharpness: ${axes.jawSharpness}")
        Log.d("DIAGNOSTIC", "eyeNarrowness: ${axes.eyeNarrowness}")
        Log.d("DIAGNOSTIC", "browWeight: ${axes.browWeight}")
        Log.d("DIAGNOSTIC", "hairDarkness: ${axes.hairDarkness}")
        Log.d("DIAGNOSTIC", "hairVolume: ${axes.hairVolume}")
        Log.d("DIAGNOSTIC", "expressionNeutrality: ${axes.expressionNeutrality}")
        Log.d("DIAGNOSTIC", "symmetry: ${axes.symmetry}")
        Log.d("DIAGNOSTIC", "contrast: ${axes.contrast}")
        Log.d("DIAGNOSTIC", "angularity: ${axes.angularity}")
        Log.d("DIAGNOSTIC", "glasses: ${axes.glasses}")
        Log.d("DIAGNOSTIC", "warmth: ${axes.warmth}")

        Log.d("DIAGNOSTIC", "STEP 4 - VERIFY DATABASE DIVERSITY")
        val featureCount = 12
        val sums = FloatArray(featureCount)
        val minVals = FloatArray(featureCount) { Float.MAX_VALUE }
        val maxVals = FloatArray(featureCount) { Float.MIN_VALUE }
        val allProfiles = characters.map { it.profile.toArray() }
        allProfiles.forEach { profile ->
            for (i in 0 until featureCount) {
                sums[i] += profile[i]
                if (profile[i] < minVals[i]) minVals[i] = profile[i]
                if (profile[i] > maxVals[i]) maxVals[i] = profile[i]
            }
        }
        val avgs = FloatArray(featureCount)
        for (i in 0 until featureCount) avgs[i] = sums[i] / characters.size
        
        val varSums = FloatArray(featureCount)
        allProfiles.forEach { profile ->
             for (i in 0 until featureCount) {
                 val diff = profile[i] - avgs[i]
                 varSums[i] += diff * diff
             }
        }
        val featureNames = listOf("faceLength", "jawSharpness", "eyeNarrowness", "browWeight", "hairDarkness", "hairVolume", "expressionNeutrality", "symmetry", "contrast", "angularity", "glasses", "warmth")
        for (i in 0 until featureCount) {
            val stdDev = Math.sqrt((varSums[i] / characters.size).toDouble()).toFloat()
            Log.d("DIAGNOSTIC", "Feature ${featureNames[i]}: min=${minVals[i]}, max=${maxVals[i]}, avg=${avgs[i]}, stdDev=$stdDev")
        }

        val results = characters.map { character ->
            val (score, distance, contributions) = SimilarityCalculator.calculateWithDetails(axes.toArray(), character.profile.toArray())
            val percentage = (score * 100).toInt().coerceIn(0, 100)
            MatchResult(character, score, percentage, distance, contributions)
        }.sortedByDescending { it.score }

        Log.d("DIAGNOSTIC", "STEP 5 & 6 - VERIFY MATCHING & DISTANCE (TOP 20)")
        results.take(20).forEachIndexed { index, res ->
            Log.d("DIAGNOSTIC", "#${index + 1} - ${res.character.name} | Score: ${res.similarityPercentage}% | Dist: ${res.distance}")
            var contribStr = ""
            res.contributions.forEach { (name, diff) ->
                contribStr += "$name: ${String.format("%.2f", diff)} | "
            }
            Log.d("DIAGNOSTIC", "   Contributions: $contribStr")
        }
        Log.d("DIAGNOSTIC", "========== DIAGNOSTIC PIPELINE END ==========")

        return results
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
