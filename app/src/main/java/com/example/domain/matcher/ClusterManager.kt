package com.example.domain.matcher

import com.example.data.model.VisualAxes

object ClusterManager {
    const val CLUSTER_A = "Cluster A - Clean & Stoic"
    const val CLUSTER_B = "Cluster B - Sharp & Cool"
    const val CLUSTER_C = "Cluster C - Mature & Structured"
    const val CLUSTER_D = "Cluster D - Highly Stylized"
    const val CLUSTER_E = "Cluster E - Non Human / Fantasy"

    val allClusters = listOf(CLUSTER_A, CLUSTER_B, CLUSTER_C, CLUSTER_D, CLUSTER_E)

    fun determineUserCluster(axes: VisualAxes): Pair<String, Float> {
        // Calculate scores for each cluster
        // Hyper Realistic: angularity high, symmetry high, expressionNeutrality high, jawSharpness high
        val scoreA = (axes.angularity + axes.symmetry + axes.expressionNeutrality + axes.jawSharpness) / 4f
        
        // Semi Realistic: balance of stylized and realistic
        val scoreB = (axes.angularity + axes.symmetry + (1f - axes.eyeNarrowness) + axes.jawSharpness) / 4f * 0.9f
        
        // Stylized Shonen: low faceLength, high jawSharpness, big eyes (low eyeNarrowness)
        val scoreC = ((1f - axes.faceLength) + axes.jawSharpness + (1f - axes.eyeNarrowness) + axes.contrast) / 4f
        
        // Highly Stylized: low faceLength, low angularity (round), big eyes
        val scoreD = ((1f - axes.faceLength) + (1f - axes.angularity) + (1f - axes.eyeNarrowness) + axes.warmth) / 4f
        
        // Non Human / Fantasy: high contrast, extreme features
        val scoreE = (axes.contrast + axes.browWeight + (1f - axes.expressionNeutrality)) / 3f * 0.8f

        val scores = mapOf(
            CLUSTER_A to scoreA,
            CLUSTER_B to scoreB,
            CLUSTER_C to scoreC,
            CLUSTER_D to scoreD,
            CLUSTER_E to scoreE
        )

        val sorted = scores.entries.sortedByDescending { it.value }
        val primary = sorted[0]
        val secondary = sorted[1]
        
        // Normalize confidence
        val total = primary.value + secondary.value
        val confidence = if (total > 0f) primary.value / total else 0.5f

        // ensure confidence is somewhat realistic between 50% and 99%
        val adjustedConfidence = (confidence * 100f).coerceIn(50f, 99f) / 100f

        return Pair(primary.key, adjustedConfidence)
    }
    
    fun getNearestClusters(primaryCluster: String): List<String> {
        val index = allClusters.indexOf(primaryCluster)
        if (index == -1) return emptyList()
        val nearest = mutableListOf<String>()
        if (index > 0) nearest.add(allClusters[index - 1])
        if (index < allClusters.size - 1) nearest.add(allClusters[index + 1])
        return nearest
    }

    fun getDesignLanguageSummary(cluster: String): String {
        return when (cluster) {
            CLUSTER_A -> "Stoic Guardian"
            CLUSTER_B -> "Elegant Warrior"
            CLUSTER_C -> "Sharp Rival"
            CLUSTER_D -> "Expressive Hero"
            CLUSTER_E -> "Soft Strategist"
            else -> "Gentle Protector"
        }
    }
}
