with open("app/src/main/java/com/example/util/ShareCardGenerator.kt", "r") as f:
    content = f.read()

new_imports = """
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
"""

content = content.replace("import android.content.Context", "import android.content.Context\n" + new_imports)

with open("app/src/main/java/com/example/util/ShareCardGenerator.kt", "w") as f:
    f.write(content)
