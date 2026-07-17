package com.example.domain.matcher

import com.example.data.model.VisualAxes
import kotlin.math.sqrt

object SimilarityCalculator {
    fun calculate(v1: FloatArray, v2: FloatArray): Float {
        if (v1.size != v2.size) return 0f
        var sumSq = 0f
        for (i in v1.indices) {
            val diff = v1[i] - v2[i]
            sumSq += diff * diff
        }
        val distance = sqrt(sumSq)
        val maxDistance = sqrt(v1.size.toFloat()) // max possible distance if all diffs are 1.0
        return (1.0f - (distance / maxDistance)).coerceIn(0f, 1f)
    }

    fun calculate(v1: VisualAxes, v2: VisualAxes): Float {
        return calculate(v1.toArray(), v2.toArray())
    }
}
