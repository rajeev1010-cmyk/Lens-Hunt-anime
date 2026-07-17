package com.example.domain.matcher

import com.example.data.model.VisualAxes
import kotlin.math.sqrt

object SimilarityCalculator {
    private val weights = floatArrayOf(
        1.0f, // 0: faceLength (Proportions 10%)
        1.5f, // 1: jawSharpness (Jaw Shape 15%)
        2.0f, // 2: eyeNarrowness (Eyes 20%)
        0.25f, // 3: browWeight (Expression 2.5%)
        0.25f, // 4: hairDarkness (Hair 2.5%)
        0.25f, // 5: hairVolume (Hair 2.5%)
        0.25f, // 6: expressionNeutrality (Expression 2.5%)
        1.0f, // 7: symmetry (Symmetry 10%)
        0.0f, // 8: contrast (0%)
        3.5f, // 9: angularity (Face Geometry 35%)
        0.0f, // 10: glasses (0%)
        0.0f  // 11: warmth (0%)
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
            contributions[featureNames.getOrElse(i) { "feature$i" }] = componentDistanceSq // Use component distance as contribution
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
