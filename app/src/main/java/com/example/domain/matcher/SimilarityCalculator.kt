package com.example.domain.matcher

import com.example.data.model.VisualAxes
import kotlin.math.sqrt

object SimilarityCalculator {
    private val weights = floatArrayOf(
        1.8f, // faceLength
        2.0f, // jawSharpness
        2.0f, // eyeNarrowness
        1.5f, // browWeight
        0.5f, // hairDarkness
        0.5f, // hairVolume
        0.7f, // expressionNeutrality
        1.5f, // symmetry
        0.6f, // contrast
        2.0f, // angularity
        0.3f, // glasses
        0.4f  // warmth
    )
    
    private val featureNames = listOf(
        "faceLength", "jawSharpness", "eyeNarrowness", "browWeight",
        "hairDarkness", "hairVolume", "expressionNeutrality", "symmetry",
        "contrast", "angularity", "glasses", "warmth"
    )

    fun calculateWithDetails(v1: FloatArray, v2: FloatArray): Triple<Float, Float, Map<String, Float>> {
        if (v1.size != v2.size) return Triple(0f, 0f, emptyMap())
        var sumSq = 0f
        var maxPossibleSumSq = 0f
        val contributions = mutableMapOf<String, Float>()
        for (i in v1.indices) {
            val diff = v1[i] - v2[i]
            val w = if (i < weights.size) weights[i] else 1.0f
            val componentDistanceSq = (diff * diff) * w
            sumSq += componentDistanceSq
            maxPossibleSumSq += 1.0f * w
            contributions[featureNames.getOrElse(i) { "feature$i" }] = diff * w // show weighted diff direction
        }
        val distance = sqrt(sumSq)
        val maxDistance = sqrt(maxPossibleSumSq)
        val score = (1.0f - (distance / maxDistance)).coerceIn(0f, 1f)
        return Triple(score, distance, contributions)
    }

    fun calculate(v1: FloatArray, v2: FloatArray): Float {
        return calculateWithDetails(v1, v2).first
    }

    fun calculate(v1: VisualAxes, v2: VisualAxes): Float {
        return calculate(v1.toArray(), v2.toArray())
    }
}
