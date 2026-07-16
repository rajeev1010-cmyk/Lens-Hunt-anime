package com.example.domain.matcher

import com.example.data.model.VisualAxes
import kotlin.math.sqrt

object CosineSimilarity {
    fun calculate(v1: FloatArray, v2: FloatArray): Float {
        if (v1.size != v2.size) return 0f
        var dotProduct = 0f
        var normA = 0f
        var normB = 0f
        for (i in v1.indices) {
            dotProduct += v1[i] * v2[i]
            normA += v1[i] * v1[i]
            normB += v2[i] * v2[i]
        }
        if (normA == 0f || normB == 0f) return 0f
        return dotProduct / (sqrt(normA) * sqrt(normB))
    }

    fun calculate(v1: VisualAxes, v2: VisualAxes): Float {
        return calculate(v1.toArray(), v2.toArray())
    }
}
