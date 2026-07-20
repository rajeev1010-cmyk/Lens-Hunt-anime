package com.example.data.model

import androidx.room.Embedded

data class VisualAxes(
    val faceLength: Float = 0.5f,
    val jawSharpness: Float = 0.5f,
    val eyeNarrowness: Float = 0.5f,
    val browWeight: Float = 0.5f,
    val hairDarkness: Float = 0.5f,
    val hairVolume: Float = 0.5f,
    val expressionNeutrality: Float = 0.5f,
    val symmetry: Float = 0.6f,
    val contrast: Float = 0.5f,
    val angularity: Float = 0.5f,
    val glasses: Float = 0.0f,
    val warmth: Float = 0.5f
) {
    fun toArray(): FloatArray = floatArrayOf(
        faceLength, jawSharpness, eyeNarrowness, browWeight,
        hairDarkness, hairVolume, expressionNeutrality, symmetry,
        contrast, angularity, glasses, warmth
    )
}
