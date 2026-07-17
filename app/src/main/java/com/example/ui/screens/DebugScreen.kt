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
import com.example.viewmodel.MainViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DebugScreen(viewModel: MainViewModel, onBack: () -> Unit) {
    val faceResult by viewModel.faceResult.collectAsState()
    val topMatches by viewModel.topMatches.collectAsState()
    val isAnalyzing by viewModel.isAnalyzing.collectAsState()

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
                Text("Detected Face Features", style = MaterialTheme.typography.titleLarge)
                if (faceResult != null) {
                    val axes = faceResult!!.axes
                    Text("faceLength: ${String.format(Locale.US, "%.3f", axes.faceLength)}")
                    Text("jawSharpness: ${String.format(Locale.US, "%.3f", axes.jawSharpness)}")
                    Text("eyeNarrowness: ${String.format(Locale.US, "%.3f", axes.eyeNarrowness)}")
                    Text("browWeight: ${String.format(Locale.US, "%.3f", axes.browWeight)}")
                    Text("hairDarkness: ${String.format(Locale.US, "%.3f", axes.hairDarkness)}")
                    Text("hairVolume: ${String.format(Locale.US, "%.3f", axes.hairVolume)}")
                    Text("expressionNeutrality: ${String.format(Locale.US, "%.3f", axes.expressionNeutrality)}")
                    Text("symmetry: ${String.format(Locale.US, "%.3f", axes.symmetry)}")
                    Text("contrast: ${String.format(Locale.US, "%.3f", axes.contrast)}")
                    Text("angularity: ${String.format(Locale.US, "%.3f", axes.angularity)}")
                    Text("glasses: ${String.format(Locale.US, "%.3f", axes.glasses)}")
                    Text("warmth: ${String.format(Locale.US, "%.3f", axes.warmth)}")
                } else {
                    Text("No face detected")
                }
            }

            item {
                Divider()
                Text("Database & Matches", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(8.dp))
                if (isAnalyzing) {
                    CircularProgressIndicator()
                }
            }

            items(topMatches.take(20)) { match ->
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
                        Text("Distance: ${String.format(Locale.US, "%.4f", match.distance)}")
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Contributions:", fontWeight = FontWeight.SemiBold)
                        match.contributions.forEach { (name, value) ->
                            Text("$name: ${String.format(Locale.US, "%+.3f", value)}")
                        }
                    }
                }
            }
        }
    }
}
