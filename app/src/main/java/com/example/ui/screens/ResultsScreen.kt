package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.domain.matcher.ClusterManager
import com.example.util.ShareCardGenerator
import com.example.viewmodel.MainViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ResultsScreen(viewModel: MainViewModel, onBack: () -> Unit) {
    val topMatches by viewModel.topMatches.collectAsState()
    val userSelfie by viewModel.userSelfie.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Result") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        if (topMatches.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("No analysis available.")
            }
        } else {
            val topMatch = topMatches.first()
            val primaryCluster = topMatch.character.cluster
            val designLanguage = topMatch.character.designLanguage

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                item {
                    // Header Card
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                    ) {
                        Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                "YOUR FACIAL DESIGN LANGUAGE",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            Text(
                                text = designLanguage,
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            if (userSelfie != null) {
                                Image(
                                    bitmap = userSelfie!!.asImageBitmap(),
                                    contentDescription = "User Selfie",
                                    modifier = Modifier
                                        .size(120.dp)
                                        .clip(RoundedCornerShape(16.dp)),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                    }
                }

                item {
                    Text(
                        "Characters That Share Your Design Language",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                items(topMatches.take(5)) { match ->
                    val char = match.character
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    "${match.similarityPercentage}% Match",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.width(16.dp))
                                Text(
                                    char.name,
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }
                            Text(char.series, style = MaterialTheme.typography.labelLarge)
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            Text("Why this character shares your design language", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Your face has:", fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.bodyMedium)
                            
                            val topFeatures = match.contributions.entries
                                .filter { it.key != "hairDarkness" && it.key != "hairVolume" && it.key != "glasses" && it.key != "contrast" && it.key != "warmth" } 
                                .sortedBy { it.value } 
                                .take(4)
                            
                            val explanations = topFeatures.map { entry ->
                                when (entry.key) {
                                    "faceLength" -> "Balanced facial proportions"
                                    "jawSharpness" -> "Specific jawline definition"
                                    "eyeNarrowness" -> "Similar eye shape & size"
                                    "browWeight" -> "Similar brow prominence"
                                    "symmetry" -> "High facial symmetry"
                                    "expressionNeutrality" -> "Similar baseline expression"
                                    "angularity" -> "Distinct facial geometry and angularity"
                                    else -> entry.key
                                }
                            }
                            
                            explanations.forEach { exp ->
                                Text("• $exp", style = MaterialTheme.typography.bodyMedium)
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "These characteristics are strongly present in ${char.name}'s character design.",
                                style = MaterialTheme.typography.bodyMedium,
                                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                            )
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            HorizontalDivider()
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            Text("Designer:", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall)
                            Text(char.designer, style = MaterialTheme.typography.bodySmall)
                            Spacer(modifier = Modifier.height(4.dp))
                            
                            Text("Principles:", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall)
                            Text(char.designPrinciples, style = MaterialTheme.typography.bodySmall)
                            Spacer(modifier = Modifier.height(4.dp))
                            
                            Text("Visual Traits:", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall)
                            Text(char.visualTraits, style = MaterialTheme.typography.bodySmall)
                            Spacer(modifier = Modifier.height(4.dp))
                            
                            Text("Design Breakdown:", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall)
                            Text(char.designBreakdown, style = MaterialTheme.typography.bodySmall)
                            Spacer(modifier = Modifier.height(4.dp))

                            Text("Description:", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall)
                            Text(char.description, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            if (userSelfie != null) {
                                coroutineScope.launch {
                                    ShareCardGenerator.generateAndShare(context, userSelfie!!, topMatch)
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Generate Share Card")
                    }
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}
