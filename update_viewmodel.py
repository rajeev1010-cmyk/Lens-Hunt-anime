import re

with open("app/src/main/java/com/example/viewmodel/MainViewModel.kt", "r") as f:
    content = f.read()

import_statement = "import com.example.domain.analyzer.GeminiService\nimport android.graphics.Bitmap"
content = content.replace("import com.example.domain.analyzer.FaceAnalyzer", import_statement + "\nimport com.example.domain.analyzer.FaceAnalyzer")

# Add the capture method
capture_method = """
    private val geminiService = GeminiService()

    fun captureAndAnalyze(bitmap: Bitmap, onComplete: () -> Unit) {
        _isAnalyzing.value = true
        _userSelfie.value = bitmap
        
        viewModelScope.launch(Dispatchers.Main) {
            val axes = geminiService.analyzeFace(bitmap)
            
            if (axes != null) {
                _userProfile.value = axes
                val (matches, debugInfo) = repository.findMatches(
                    axes,
                    _faceResult.value?.visualPresentation ?: "unknown",
                    _faceResult.value?.presentationConfidence ?: 0.5f,
                    _genderFilter.value
                )
                _topMatches.value = matches
                _matchDebugInfo.value = debugInfo
                saveCurrentMatch()
            }
            
            _isAnalyzing.value = false
            onComplete()
        }
    }
"""

content = content.replace("fun saveSelfie(bitmap: android.graphics.Bitmap) {", capture_method + "\n    fun saveSelfie(bitmap: android.graphics.Bitmap) {")

with open("app/src/main/java/com/example/viewmodel/MainViewModel.kt", "w") as f:
    f.write(content)
