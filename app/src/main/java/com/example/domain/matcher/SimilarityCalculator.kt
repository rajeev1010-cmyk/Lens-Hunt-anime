package com.example.domain.matcher

import com.example.data.model.VisualAxes
import kotlin.math.sqrt

object SimilarityCalculator {
    private val weights = floatArrayOf(
        1.5f, // 0: faceShape
        1.5f, // 1: jawWidthRatio
        1.5f, // 2: jawAngle
        1.0f, // 3: chinLengthRatio
        1.5f, // 4: chinSharpness
        0.5f, // 5: foreheadWidthRatio
        0.5f, // 6: foreheadHeightRatio
        1.0f, // 7: cheekboneWidthRatio
        1.5f, // 8: faceHeightRatio
        2.0f, // 9: eyeSizeRatio
        1.0f, // 10: eyeSpacingRatio
        1.5f, // 11: eyeTilt
        2.0f, // 12: eyeRoundness
        1.0f, // 13: eyebrowThickness
        1.0f, // 14: eyebrowCurve
        0.5f, // 15: noseLengthRatio
        0.5f, // 16: noseWidthRatio
        1.0f, // 17: mouthWidthRatio
        1.0f, // 18: lipThickness
        0.5f, // 19: earSizeRatio
        0.5f, // 20: hairlineHeight
        0.5f, // 21: neckWidthRatio
        1.0f, // 22: symmetry
        1.0f  // 23: expressionNeutrality
    )
    
    private val featureNames = listOf(
        "faceShape", "jawWidthRatio", "jawAngle", "chinLengthRatio", "chinSharpness",
        "foreheadWidthRatio", "foreheadHeightRatio", "cheekboneWidthRatio", "faceHeightRatio",
        "eyeSizeRatio", "eyeSpacingRatio", "eyeTilt", "eyeRoundness",
        "eyebrowThickness", "eyebrowCurve",
        "noseLengthRatio", "noseWidthRatio",
        "mouthWidthRatio", "lipThickness",
        "earSizeRatio", "hairlineHeight", "neckWidthRatio",
        "symmetry", "expressionNeutrality"
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
            contributions[featureNames.getOrElse(i) { "feature\$i" }] = componentDistanceSq
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
