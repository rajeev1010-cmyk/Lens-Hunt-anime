package com.example.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.model.VisualAxes
import com.example.viewmodel.MainViewModel
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.PI

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultsScreen(viewModel: MainViewModel, onBack: () -> Unit) {
    val topMatches by viewModel.topMatches.collectAsState()
    val userProfile by viewModel.userProfile.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Analysis Results") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        if (topMatches.isEmpty() || userProfile == null) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("No analysis available.")
            }
        } else {
            val topMatch = topMatches.first()
            val character = topMatch.character
            
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Your facial design most closely resembles...",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = character.name,
                                style = MaterialTheme.typography.headlineLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${character.series} • ${character.studio}",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "${topMatch.similarityPercentage}% Match",
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }

                item {
                    RadarChartCard(
                        userAxes = userProfile!!,
                        characterAxes = character.profile
                    )
                }

                item {
                    Text("Design Breakdown", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Description", fontWeight = FontWeight.SemiBold)
                            Text(character.description, style = MaterialTheme.typography.bodyMedium)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Communicates", fontWeight = FontWeight.SemiBold)
                            Text(character.communicates, style = MaterialTheme.typography.bodyMedium)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Through", fontWeight = FontWeight.SemiBold)
                            Text(character.through, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }

                item {
                    Text("Top Alternatives", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                }

                items(topMatches.drop(1).take(4).size) { index ->
                    val match = topMatches[index + 1]
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.padding(16.dp).fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(match.character.name, fontWeight = FontWeight.SemiBold)
                                Text(match.character.series, style = MaterialTheme.typography.bodySmall)
                            }
                            Text("${match.similarityPercentage}%", color = MaterialTheme.colorScheme.secondary)
                        }
                    }
                }
                
                item { Spacer(modifier = Modifier.height(32.dp)) }
            }
        }
    }
}

@Composable
fun RadarChartCard(userAxes: VisualAxes, characterAxes: VisualAxes) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Feature Comparison", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            
            val userColor = MaterialTheme.colorScheme.primary
            val charColor = MaterialTheme.colorScheme.tertiary
            
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(12.dp).background(userColor, RoundedCornerShape(50)))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("You", style = MaterialTheme.typography.bodySmall)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(12.dp).background(charColor, RoundedCornerShape(50)))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Character", style = MaterialTheme.typography.bodySmall)
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))

            Canvas(modifier = Modifier.size(250.dp)) {
                val center = Offset(size.width / 2, size.height / 2)
                val radius = size.minDimension / 2 - 20.dp.toPx()
                val numAxes = 10
                
                val userArray = floatArrayOf(userAxes.faceLength, userAxes.jawSharpness, userAxes.eyeNarrowness, userAxes.browWeight, userAxes.hairDarkness, userAxes.hairVolume, userAxes.expressionNeutrality, userAxes.symmetry, userAxes.contrast, userAxes.angularity)
                val charArray = floatArrayOf(characterAxes.faceLength, characterAxes.jawSharpness, characterAxes.eyeNarrowness, characterAxes.browWeight, characterAxes.hairDarkness, characterAxes.hairVolume, characterAxes.expressionNeutrality, characterAxes.symmetry, characterAxes.contrast, characterAxes.angularity)
                
                // Draw Web
                for (i in 1..4) {
                    val stepRadius = radius * (i / 4f)
                    val path = Path()
                    for (j in 0 until numAxes) {
                        val angle = 2 * PI * j / numAxes - PI / 2
                        val x = center.x + stepRadius * cos(angle).toFloat()
                        val y = center.y + stepRadius * sin(angle).toFloat()
                        if (j == 0) path.moveTo(x, y) else path.lineTo(x, y)
                    }
                    path.close()
                    drawPath(path, color = Color.Gray.copy(alpha = 0.3f), style = Stroke(1f))
                }

                // Draw Axes Lines
                for (j in 0 until numAxes) {
                    val angle = 2 * PI * j / numAxes - PI / 2
                    val x = center.x + radius * cos(angle).toFloat()
                    val y = center.y + radius * sin(angle).toFloat()
                    drawLine(Color.Gray.copy(alpha = 0.5f), center, Offset(x, y), 1f)
                }
                
                // Draw Character Shape
                val charPath = Path()
                for (j in 0 until numAxes) {
                    val angle = 2 * PI * j / numAxes - PI / 2
                    val valRadius = radius * charArray[j]
                    val x = center.x + valRadius * cos(angle).toFloat()
                    val y = center.y + valRadius * sin(angle).toFloat()
                    if (j == 0) charPath.moveTo(x, y) else charPath.lineTo(x, y)
                }
                charPath.close()
                drawPath(charPath, color = charColor.copy(alpha = 0.5f))
                drawPath(charPath, color = charColor, style = Stroke(3f))
                
                // Draw User Shape
                val userPath = Path()
                for (j in 0 until numAxes) {
                    val angle = 2 * PI * j / numAxes - PI / 2
                    val valRadius = radius * userArray[j]
                    val x = center.x + valRadius * cos(angle).toFloat()
                    val y = center.y + valRadius * sin(angle).toFloat()
                    if (j == 0) userPath.moveTo(x, y) else userPath.lineTo(x, y)
                }
                userPath.close()
                drawPath(userPath, color = userColor.copy(alpha = 0.4f))
                drawPath(userPath, color = userColor, style = Stroke(4f))
            }
        }
    }
}
