import re

with open("app/src/main/java/com/example/util/ShareCardGenerator.kt", "r") as f:
    content = f.read()

# Make generateAndShare take faceBox if we want, or just add face detection inside?
# Let's add face detection inside using ML Kit.

new_imports = """
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
"""

content = content.replace("import android.graphics.Typeface", new_imports + "\nimport android.graphics.Typeface")

detect_face_func = """
    private suspend fun detectFace(bitmap: Bitmap): android.graphics.Rect? = suspendCoroutine { continuation ->
        val options = FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
            .build()
        val detector = FaceDetection.getClient(options)
        val image = InputImage.fromBitmap(bitmap, 0)
        detector.process(image)
            .addOnSuccessListener { faces ->
                if (faces.isNotEmpty()) {
                    continuation.resume(faces.first().boundingBox)
                } else {
                    continuation.resume(null)
                }
            }
            .addOnFailureListener {
                continuation.resume(null)
            }
    }
"""

content = content.replace("private fun scaleAndCropCenter", detect_face_func + "\n    private fun scaleAndCropCenter")

scale_func_old = """private fun scaleAndCropCenter(bitmap: Bitmap, newWidth: Int, newHeight: Int): Bitmap {"""
scale_func_new = """private fun scaleAndCropCenter(bitmap: Bitmap, newWidth: Int, newHeight: Int, faceBox: android.graphics.Rect? = null): Bitmap {"""

content = content.replace(scale_func_old, scale_func_new)

math_logic_old = """        val left = (newWidth - scaledWidth) / 2
        val top = (newHeight - scaledHeight) / 2"""

math_logic_new = """        var left = (newWidth - scaledWidth) / 2f
        var top = (newHeight - scaledHeight) / 2f

        if (faceBox != null) {
            val faceCenterX = faceBox.exactCenterX() * scale
            val faceCenterY = faceBox.exactCenterY() * scale
            left = newWidth / 2f - faceCenterX
            top = newHeight / 2f - faceCenterY
            
            // Constrain left and top
            left = left.coerceIn(newWidth - scaledWidth, 0f)
            top = top.coerceIn(newHeight - scaledHeight, 0f)
        }"""

content = content.replace(math_logic_old, math_logic_new)

# Update the call to scaleAndCropCenter
call_old = """val scaledSelfie = scaleAndCropCenter(selfie, photoWidth.toInt(), photoHeight.toInt())"""
call_new = """val faceBox = detectFace(selfie)
        val scaledSelfie = scaleAndCropCenter(selfie, photoWidth.toInt(), photoHeight.toInt(), faceBox)"""

content = content.replace(call_old, call_new)

with open("app/src/main/java/com/example/util/ShareCardGenerator.kt", "w") as f:
    f.write(content)

