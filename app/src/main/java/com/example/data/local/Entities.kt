package com.example.data.local

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.data.model.VisualAxes

@Entity(tableName = "characters")
data class CharacterEntity(
    @PrimaryKey val id: String,
    val name: String,
    val series: String,
    val designer: String,
    val studio: String,
    val franchise: String,
    val collection: String,
    val cluster: String,
    val visualTraits: String, 
    val designLanguage: String, 
    val shapeLanguage: String,
    val primaryColors: String, 
    val silhouette: String,
    val archetype: String,
    val temperament: String,
    val keywords: String, 
    val copyrightNotice: String,
    val description: String,
    @Embedded val profile: VisualAxes,
    val communicates: String, 
    val through: String 
)

@Entity(tableName = "history")
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestamp: Long,
    val matchedCharacterId: String,
    val similarityScore: Float,
    val imageUri: String? = null,
    @Embedded(prefix = "user_") val userProfile: VisualAxes
)

@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey val characterId: String,
    val addedAt: Long
)

@Entity(tableName = "settings")
data class SettingsEntity(
    @PrimaryKey val id: Int = 1,
    val theme: String = "SYSTEM",
    val animationSpeed: Float = 1.0f,
    val cameraQuality: String = "HIGH",
    val analysisSensitivity: Float = 0.5f,
    val developerMode: Boolean = false
)
