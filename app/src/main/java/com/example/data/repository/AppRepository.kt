package com.example.data.repository

import com.example.data.local.AppDao
import com.example.data.local.CharacterDataSeed
import com.example.data.local.CharacterEntity
import com.example.data.local.HistoryEntity
import com.example.data.local.SettingsEntity
import com.example.data.model.VisualAxes
import com.example.domain.matcher.CosineSimilarity
import com.example.domain.matcher.MatchResult
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

        return characters.map { character ->
            val score = CosineSimilarity.calculate(axes, character.profile)
            val percentage = (score * 100).toInt().coerceIn(0, 100)
            MatchResult(character, score, percentage)
        }.sortedByDescending { it.score }
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
