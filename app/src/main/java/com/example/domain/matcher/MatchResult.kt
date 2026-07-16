package com.example.domain.matcher

import com.example.data.local.CharacterEntity

data class MatchResult(
    val character: CharacterEntity,
    val score: Float,
    val similarityPercentage: Int
)
