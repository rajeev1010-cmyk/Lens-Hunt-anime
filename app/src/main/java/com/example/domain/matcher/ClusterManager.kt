package com.example.domain.matcher

import com.example.data.model.VisualAxes

object ClusterManager {
    const val CLUSTER_A = "Cluster A - Clean & Stoic"
    const val CLUSTER_B = "Cluster B - Sharp & Cool"
    const val CLUSTER_C = "Cluster C - Soft & Cute"
    const val CLUSTER_D = "Cluster D - Expressive & Bold"
    const val CLUSTER_E = "Cluster E - Unique & Ethereal"

    val allClusters = listOf(CLUSTER_A, CLUSTER_B, CLUSTER_C, CLUSTER_D, CLUSTER_E)

    fun determineUserCluster(axes: VisualAxes): Pair<String, Float> {
        val s = axes.stylizationIndex
        
        val primaryCluster = when {
            s >= 0.8f -> CLUSTER_D
            s <= 0.3f -> CLUSTER_B
            // The python script used gender == "female" for C. We don't have gender here, 
            // so we'll use a mix of faceShape and stylizationIndex
            axes.faceShape < 0.45f && axes.eyeSizeRatio > 0.6f -> CLUSTER_C
            s >= 0.5f -> CLUSTER_A
            else -> CLUSTER_E
        }
        
        // Confidence could just be based on distance from the threshold
        val confidence = when(primaryCluster) {
            CLUSTER_D -> (s - 0.7f) / 0.3f // 0.8+ is high confidence
            CLUSTER_B -> (0.4f - s) / 0.4f // <=0.3 is high confidence
            CLUSTER_C -> (axes.eyeSizeRatio + (1f - axes.faceShape)) / 2f
            CLUSTER_A -> (s - 0.4f) / 0.4f
            else -> 0.6f
        }
        
        val adjustedConfidence = (0.5f + (confidence * 0.49f)).coerceIn(0.5f, 0.99f)

        return Pair(primaryCluster, adjustedConfidence)
    }

    fun getNearestClusters(primaryCluster: String): List<String> {
        return when (primaryCluster) {
            CLUSTER_D -> listOf(CLUSTER_C, CLUSTER_A)
            CLUSTER_B -> listOf(CLUSTER_E, CLUSTER_A)
            CLUSTER_C -> listOf(CLUSTER_D, CLUSTER_E)
            CLUSTER_A -> listOf(CLUSTER_B, CLUSTER_D)
            CLUSTER_E -> listOf(CLUSTER_C, CLUSTER_B)
            else -> emptyList()
        }
    }

    fun getDesignLanguageSummary(cluster: String): String {
        return when (cluster) {
            CLUSTER_A -> "Stoic Guardian"
            CLUSTER_B -> "Elegant Warrior"
            CLUSTER_C -> "Soft Strategist"
            CLUSTER_D -> "Expressive Hero"
            CLUSTER_E -> "Unique & Ethereal"
            else -> "Gentle Protector"
        }
    }
}
