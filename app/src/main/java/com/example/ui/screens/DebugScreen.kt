package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.domain.matcher.ClusterManager
import com.example.viewmodel.MainViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DebugScreen(viewModel: MainViewModel, onBack: () -> Unit) {
    val faceResult by viewModel.faceResult.collectAsState()
    val topMatches by viewModel.topMatches.collectAsState()
    val isAnalyzing by viewModel.isAnalyzing.collectAsState()
    val allCharacters by viewModel.allCharacters.collectAsState()

    val matchDebugInfo by viewModel.matchDebugInfo.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Diagnostics") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text("Database & Metadata", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(8.dp))
                
                val missingMetadata = allCharacters.filter { char ->
                    char.designer.isBlank() || char.designer == "Unknown" ||
                    char.designLanguage.isBlank() || char.designLanguage == "Design" ||
                    char.designPrinciples.isBlank() || char.designPrinciples == "Unknown" ||
                    char.visualTraits.isBlank() || char.visualTraits == "Trait" ||
                    char.designBreakdown.isBlank() || char.designBreakdown == "Unknown" ||
                    char.description.isBlank() || char.description == "Desc"
                }
                
                Text("Database Size: ${allCharacters.size} Characters", fontWeight = FontWeight.SemiBold)
                Text("Characters Loaded: ${allCharacters.size}")
                Text("Character Metadata Loaded: ${allCharacters.size - missingMetadata.size} fully loaded")
                
                Spacer(modifier = Modifier.height(8.dp))
                Text("Missing Metadata Report:", fontWeight = FontWeight.SemiBold)
                if (missingMetadata.isEmpty()) {
                    Text("All characters have complete metadata.", color = MaterialTheme.colorScheme.primary)
                } else {
                    missingMetadata.forEach {
                        Text("- ${it.name} (${it.id})", color = MaterialTheme.colorScheme.error)
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider()
            }

            item {
                Text("Visual Presentation Filter", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(8.dp))
                matchDebugInfo?.let { debugInfo ->
                    Text("Detected Visual Presentation: ${debugInfo.visualPresentation}")
                    Text("Presentation Confidence: ${String.format(Locale.US, "%.1f", debugInfo.presentationConfidence * 100)}%")
                    Text("Characters Loaded: ${debugInfo.charactersLoaded}")
                    Text("Characters After Filtering: ${debugInfo.charactersAfterFiltering}")
                } ?: run {
                    Text("No presentation data available.")
                }
                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider()
            }
            item {
                Text("Detected Face Features", style = MaterialTheme.typography.titleLarge)
                if (faceResult != null) {
                    val axes = faceResult!!.axes
                    Text("faceShape: ${String.format(Locale.US, "%.3f", axes.faceShape)}")
                    Text("jawWidthRatio: ${String.format(Locale.US, "%.3f", axes.jawWidthRatio)}")
                    Text("jawAngle: ${String.format(Locale.US, "%.3f", axes.jawAngle)}")
                    Text("chinLengthRatio: ${String.format(Locale.US, "%.3f", axes.chinLengthRatio)}")
                    Text("chinSharpness: ${String.format(Locale.US, "%.3f", axes.chinSharpness)}")
                    Text("foreheadWidthRatio: ${String.format(Locale.US, "%.3f", axes.foreheadWidthRatio)}")
                    Text("foreheadHeightRatio: ${String.format(Locale.US, "%.3f", axes.foreheadHeightRatio)}")
                    Text("cheekboneWidthRatio: ${String.format(Locale.US, "%.3f", axes.cheekboneWidthRatio)}")
                    Text("faceHeightRatio: ${String.format(Locale.US, "%.3f", axes.faceHeightRatio)}")
                    Text("eyeSizeRatio: ${String.format(Locale.US, "%.3f", axes.eyeSizeRatio)}")
                    Text("eyeSpacingRatio: ${String.format(Locale.US, "%.3f", axes.eyeSpacingRatio)}")
                    Text("eyeTilt: ${String.format(Locale.US, "%.3f", axes.eyeTilt)}")
                    Text("eyeRoundness: ${String.format(Locale.US, "%.3f", axes.eyeRoundness)}")
                    Text("eyebrowThickness: ${String.format(Locale.US, "%.3f", axes.eyebrowThickness)}")
                    Text("eyebrowCurve: ${String.format(Locale.US, "%.3f", axes.eyebrowCurve)}")
                    Text("noseLengthRatio: ${String.format(Locale.US, "%.3f", axes.noseLengthRatio)}")
                    Text("noseWidthRatio: ${String.format(Locale.US, "%.3f", axes.noseWidthRatio)}")
                    Text("mouthWidthRatio: ${String.format(Locale.US, "%.3f", axes.mouthWidthRatio)}")
                    Text("lipThickness: ${String.format(Locale.US, "%.3f", axes.lipThickness)}")
                    Text("earSizeRatio: ${String.format(Locale.US, "%.3f", axes.earSizeRatio)}")
                    Text("hairlineHeight: ${String.format(Locale.US, "%.3f", axes.hairlineHeight)}")
                    Text("neckWidthRatio: ${String.format(Locale.US, "%.3f", axes.neckWidthRatio)}")
                    Text("symmetry: ${String.format(Locale.US, "%.3f", axes.symmetry)}")
                    Text("expressionNeutrality: ${String.format(Locale.US, "%.3f", axes.expressionNeutrality)}")
                    Text("stylizationIndex: ${String.format(Locale.US, "%.3f", axes.stylizationIndex)}")
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Hierarchical Cluster", style = MaterialTheme.typography.titleLarge)
                    
                    val (primaryCluster, confidence) = ClusterManager.determineUserCluster(axes)
                    val primaryLanguage = ClusterManager.getDesignLanguageSummary(primaryCluster)
                    
                    Text("Primary Design Language: $primaryLanguage", fontWeight = FontWeight.Bold)
                    Text("Detected Cluster: $primaryCluster")
                    Text("Cluster Confidence: ${String.format(Locale.US, "%.1f", confidence * 100)}%")
                    
                    if (confidence < 0.7f) {
                        val nearest = ClusterManager.getNearestClusters(primaryCluster)
                        Text("Low confidence. Searching neighbours:", color = MaterialTheme.colorScheme.error)
                        nearest.forEach {
                            Text("- $it")
                        }
                    }
                } else {
                    Text("No face detected")
                }
            }

            item {
                HorizontalDivider()
                Text("Top 10 Matches", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(8.dp))
                if (isAnalyzing) {
                    CircularProgressIndicator()
                }
            }
            items(topMatches.take(10)) { match ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "${match.character.name} (${match.similarityPercentage}%)",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text("Cluster: ${match.character.cluster}", style = MaterialTheme.typography.bodySmall)
                        Text("Similarity Score: ${String.format(Locale.US, "%.4f", match.score)}")
                        Text("Euclidean Distance: ${String.format(Locale.US, "%.4f", match.distance)}")
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Feature Contributions:", fontWeight = FontWeight.SemiBold)
                        match.contributions.entries.sortedBy { it.value }.forEach { (name, value) ->
                            Text("$name: ${String.format(Locale.US, "%.3f", value)}")
                        }
                    }
                }
            }
        }
    }
}
