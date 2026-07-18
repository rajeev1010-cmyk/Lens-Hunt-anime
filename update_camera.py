import re

with open("app/src/main/java/com/example/ui/screens/CameraScreen.kt", "r") as f:
    content = f.read()

# Change visibility to faceResult != null
content = content.replace("visible = topMatches.isNotEmpty(),", "visible = faceResult != null,")

# Replace onClick
old_onclick = """onClick = { 
                        previewViewRef?.bitmap?.let { viewModel.saveSelfie(it) }
                        viewModel.saveCurrentMatch()
                        onNavigateToResults() 
                    },"""

new_onclick = """onClick = { 
                        val bitmap = previewViewRef?.bitmap
                        if (bitmap != null) {
                            viewModel.captureAndAnalyze(bitmap) {
                                onNavigateToResults()
                            }
                        }
                    },"""
content = content.replace(old_onclick, new_onclick)

# Add a loading overlay if isAnalyzing
loading_overlay = """
        if (isAnalyzing) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.6f)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(color = Color.White)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Analyzing with Gemini...", color = Color.White, style = MaterialTheme.typography.titleMedium)
                }
            }
        }
    }
}
"""
content = content.replace("    }\n}", loading_overlay)

with open("app/src/main/java/com/example/ui/screens/CameraScreen.kt", "w") as f:
    f.write(content)
