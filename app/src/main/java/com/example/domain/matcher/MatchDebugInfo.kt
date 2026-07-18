package com.example.domain.matcher

data class MatchDebugInfo(
    val visualPresentation: String = "",
    val presentationConfidence: Float = 0f,
    val charactersLoaded: Int = 0,
    val charactersAfterFiltering: Int = 0
)
