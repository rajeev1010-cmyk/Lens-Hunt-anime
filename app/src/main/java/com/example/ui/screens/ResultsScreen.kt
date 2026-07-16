package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultsScreen(viewModel: MainViewModel, onBack: () -> Unit) {
    val topMatches by viewModel.topMatches.collectAsState()
    val userSelfie by viewModel.userSelfie.collectAsState()

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
            val character = topMatch.character
            
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                item {
                    // Polaroid Character Card
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                "YOUR ANIME TWIN",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            
                            if (userSelfie != null) {
                                Image(
                                    bitmap = userSelfie!!.asImageBitmap(),
                                    contentDescription = "User Selfie",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .aspectRatio(3f/4f)
                                        .clip(RoundedCornerShape(8.dp)),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                Box(modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(3f/4f)
                                    .background(Color.Gray, RoundedCornerShape(8.dp)))
                            }
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            Text(
                                "${topMatch.similarityPercentage}% Match",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text("Closest Design Match", style = MaterialTheme.typography.labelMedium)
                            Text(character.name, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                            Text(character.series, style = MaterialTheme.typography.bodyLarge)
                        }
                    }
                }

                item {
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("WHY THIS MATCH", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    val traits = character.visualTraits.split(",").map { it.trim() }
                    traits.forEach { trait ->
                        Text("• $trait", style = MaterialTheme.typography.bodyMedium)
                    }
                }

                item {
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("CHARACTER DESIGN PRINCIPLES", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    val principles = character.description.split(".").filter { it.isNotBlank() }
                    principles.forEach { principle ->
                        Text("• ${principle.trim()}", style = MaterialTheme.typography.bodyMedium)
                    }
                }

                item {
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("CHARACTER DESIGNER", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(character.designer, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
                    Text(character.studio, style = MaterialTheme.typography.bodyMedium)
                    // If no bio is present, just a generic one
                    Text("Known for expressive facial construction, refined proportions, and realistic character designs.", style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(top = 4.dp))
                }

                item {
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("DESIGN LANGUAGE", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        val tags = character.designLanguage.split(",").map { it.trim() }
                        tags.take(4).forEach { tag ->
                            SuggestionChip(onClick = {}, label = { Text(tag) })
                        }
                    }
                }

                item {
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("VISUAL TRAITS", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    @OptIn(ExperimentalLayoutApi::class)
                    FlowRow(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        val keywords = character.keywords.split(",").map { it.trim() }
                        keywords.take(6).forEach { chip ->
                            FilterChip(selected = true, onClick = {}, label = { Text(chip) })
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { /* Generate Share Card */ },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Generate Share Card")
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "This comparison is based on artistic facial design similarities and does not identify real people.",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(bottom = 32.dp)
                    )
                }
            }
        }
    }
}
