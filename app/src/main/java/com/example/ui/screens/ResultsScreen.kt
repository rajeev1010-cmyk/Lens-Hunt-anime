package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
            val designLanguage = ClusterManager.getDesignLanguageSummary(primaryCluster)

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
                        "TOP 5 EXAMPLES OF YOUR DESIGN LANGUAGE",
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
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Matched because of:", fontWeight = FontWeight.Bold)
                            
                            // Explain based on contributions
                            // We sort contributions to find features that were closest (i.e. smallest diff squared)
                            // Actually, small diff squared means they match well.
                            val topFeatures = match.contributions.entries
                                .filter { it.key != "hairDarkness" && it.key != "hairVolume" || it.value < 0.05f } // low influence for hair
                                .sortedBy { it.value } // Smallest diff means best match
                                .take(4)
                                
                            val explanations = topFeatures.map { entry ->
                                when (entry.key) {
                                    "faceLength" -> "Balanced facial proportions"
                                    "jawSharpness" -> "Specific jawline definition"
                                    "eyeNarrowness" -> "Similar eye shape & size"
                                    "browWeight" -> "Similar brow prominence"
                                    "symmetry" -> "Comparable facial symmetry"
                                    "expressionNeutrality" -> "Similar baseline expression"
                                    "angularity" -> "Similar facial roundness/angularity"
                                    "contrast" -> "Similar contrast levels"
                                    "warmth" -> "Similar warmth/coolness"
                                    "glasses" -> "Eyewear structure"
                                    "hairDarkness" -> "Similar hair shading"
                                    "hairVolume" -> "Similar hair volume"
                                    else -> entry.key
                                }
                            }
                            
                            explanations.forEach { exp ->
                                Text("• $exp", style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            if (userSelfie != null && topMatch != null) {
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
