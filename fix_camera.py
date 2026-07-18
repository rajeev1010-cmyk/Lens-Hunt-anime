import re

with open("app/src/main/java/com/example/ui/screens/CameraScreen.kt", "r") as f:
    content = f.read()

content = content.replace("import com.example.domain.analyzer.FaceAnalyzer", "import com.example.domain.analyzer.FaceAnalyzer\nimport androidx.compose.material3.CircularProgressIndicator")

with open("app/src/main/java/com/example/ui/screens/CameraScreen.kt", "w") as f:
    f.write(content)
