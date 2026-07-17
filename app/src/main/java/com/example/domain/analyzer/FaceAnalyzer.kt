package com.example.domain.analyzer

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import com.google.mlkit.vision.face.FaceLandmark
import com.example.data.model.VisualAxes
import kotlin.math.abs
import kotlin.math.max

class FaceAnalyzer {
    private val options = FaceDetectorOptions.Builder()
        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
        .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
        .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
        .setContourMode(FaceDetectorOptions.CONTOUR_MODE_ALL)
        .build()

    private val detector = FaceDetection.getClient(options)

    @SuppressLint("UnsafeOptInUsageError")
    fun analyze(imageProxy: ImageProxy, onResult: (FaceAnalysisResult?) -> Unit) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            detector.process(image)
                .addOnSuccessListener { faces ->
                    if (faces.isNotEmpty()) {
                        val face = faces.first()
                        val axes = extractFeatures(face)
                        onResult(FaceAnalysisResult(axes, face.boundingBox, image.width, image.height, imageProxy.imageInfo.rotationDegrees))
                    } else {
                        onResult(null)
                    }
                }
                .addOnFailureListener {
                    onResult(null)
                }
                .addOnCompleteListener {
                    imageProxy.close()
                }
        } else {
            imageProxy.close()
        }
    }

    fun analyzeUri(context: Context, uri: Uri, onResult: (FaceAnalysisResult?, Bitmap?) -> Unit) {
        try {
            val inputImage = InputImage.fromFilePath(context, uri)
            // Load bitmap for UI
            val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val source = ImageDecoder.createSource(context.contentResolver, uri)
                ImageDecoder.decodeBitmap(source) { decoder, _, _ ->
                    decoder.isMutableRequired = true
                }
            } else {
                @Suppress("DEPRECATION")
                MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
            }
            
            detector.process(inputImage)
                .addOnSuccessListener { faces ->
                    if (faces.isNotEmpty()) {
                        val face = faces.first()
                        val axes = extractFeatures(face)
                        onResult(FaceAnalysisResult(axes, face.boundingBox, inputImage.width, inputImage.height, 0), bitmap)
                    } else {
                        onResult(null, bitmap)
                    }
                }
                .addOnFailureListener {
                    onResult(null, bitmap)
                }
        } catch (e: Exception) {
            e.printStackTrace()
            onResult(null, null)
        }
    }

    private fun extractFeatures(face: Face): VisualAxes {
        val boundingBox = face.boundingBox
        val width = boundingBox.width().toFloat()
        val height = boundingBox.height().toFloat()
        
        // Face Length (Height / Width ratio, normalized roughly between 0.3 and 0.8)
        val rawRatio = height / max(width, 1f)
        val faceLength = (rawRatio - 1.0f).coerceIn(0f, 1f) * 0.8f + 0.2f

        // Eye Narrowness (Based on eye open probability)
        val leftOpen = face.leftEyeOpenProbability ?: 0.5f
        val rightOpen = face.rightEyeOpenProbability ?: 0.5f
        val avgOpen = (leftOpen + rightOpen) / 2f
        val eyeNarrowness = 1.0f - avgOpen

        // Expression Neutrality (Based on smiling probability)
        val smileProb = face.smilingProbability ?: 0f
        val expressionNeutrality = 1.0f - smileProb

        // Symmetry (Compare left/right landmarks roughly)
        val leftEye = face.getLandmark(FaceLandmark.LEFT_EYE)?.position
        val rightEye = face.getLandmark(FaceLandmark.RIGHT_EYE)?.position
        var symmetry = 0.5f
        if (leftEye != null && rightEye != null) {
            val midX = boundingBox.exactCenterX()
            val leftDist = abs(midX - leftEye.x)
            val rightDist = abs(rightEye.x - midX)
            val diff = abs(leftDist - rightDist) / max(width, 1f)
            symmetry = (1.0f - diff * 2f).coerceIn(0f, 1f)
        }

        // Jaw Sharpness - proxy using euler angles/size ratio
        val jawSharpness = ((width / height) * 0.7f).coerceIn(0.2f, 0.8f)

        // Contrast / Angularity / Warmth - pseudo random deterministic
        val seed = ((smileProb * 100).toInt() + (leftOpen * 100).toInt()) % 100 / 100f
        val angularity = 0.3f + seed * 0.4f
        val contrast = 0.4f + seed * 0.3f
        val warmth = 0.5f

        // Brow weight / Hair volume - approximations
        val browWeight = 0.4f + (1f - expressionNeutrality) * 0.2f
        val hairDarkness = 0.5f
        val hairVolume = 0.5f
        val glasses = 0.0f

        return VisualAxes(
            faceLength = faceLength,
            jawSharpness = jawSharpness,
            eyeNarrowness = eyeNarrowness,
            browWeight = browWeight,
            hairDarkness = hairDarkness,
            hairVolume = hairVolume,
            expressionNeutrality = expressionNeutrality,
            symmetry = symmetry,
            contrast = contrast,
            angularity = angularity,
            glasses = glasses,
            warmth = warmth
        )
    }
}
