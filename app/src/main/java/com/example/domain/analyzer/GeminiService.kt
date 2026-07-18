package com.example.domain.analyzer

import android.graphics.Bitmap
import com.example.data.model.VisualAxes
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.gianthunt.lenshunt.BuildConfig
import org.json.JSONObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GeminiService {
    private val apiKey = BuildConfig.GEMINI_API_KEY
    private val model by lazy {
        if (apiKey.isNotBlank()) {
            GenerativeModel(
                modelName = "gemini-1.5-pro",
                apiKey = apiKey
            )
        } else {
            null
        }
    }

    private val promptText = """
        Analyze this face and extract the 24 facial metrics into the following JSON schema. 
        Return ONLY valid JSON matching this exact schema.
        {
          "faceShape": number, // 0=soft/round, 1=angular/sharp
          "jawWidthRatio": number, // 0=narrow, 1=wide relative to face width
          "jawAngle": number, // 0=rounded angle, 1=sharp V-shape
          "chinLengthRatio": number,
          "chinSharpness": number, // 0=rounded, 1=pointed
          "foreheadWidthRatio": number,
          "foreheadHeightRatio": number,
          "cheekboneWidthRatio": number,
          "faceHeightRatio": number, // overall face length : width
          "eyeSizeRatio": number, // relative to face height
          "eyeSpacingRatio": number,
          "eyeTilt": number, // 0=downturned, 1=upturned
          "eyeRoundness": number, // 0=narrow/almond, 1=round
          "eyebrowThickness": number,
          "eyebrowCurve": number, // 0=straight/angled, 1=curved/soft
          "noseLengthRatio": number,
          "noseWidthRatio": number,
          "mouthWidthRatio": number,
          "lipThickness": number,
          "earSizeRatio": number,
          "hairlineHeight": number,
          "neckWidthRatio": number,
          "symmetry": number, // 0=asymmetric, 1=highly symmetric
          "expressionNeutrality": number, // 0=expressive/smiling, 1=neutral
          "stylizationIndex": number // 0=realistic, 1=highly stylized (usually ~0.5 for humans)
        }
    """.trimIndent()

    suspend fun analyzeFace(bitmap: Bitmap): VisualAxes? = withContext(Dispatchers.IO) {
        if (model == null) {
            System.err.println("Gemini API Key is missing. Please add it to AI Studio Secrets.")
            return@withContext null
        }
        
        // Convert to software bitmap if it's hardware, to avoid crashes during scaling or Gemini encoding
        val softwareBitmap = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O && bitmap.config == Bitmap.Config.HARDWARE) {
            try {
                bitmap.copy(Bitmap.Config.ARGB_8888, false) ?: bitmap
            } catch (e: Exception) {
                bitmap
            }
        } else {
            bitmap
        }
        
        // Scale down the bitmap to prevent OOM
        val maxDim = 800
        val scale = Math.min(maxDim.toFloat() / softwareBitmap.width, maxDim.toFloat() / softwareBitmap.height)
        val scaledBitmap = if (scale < 1.0f) {
            try {
                Bitmap.createScaledBitmap(softwareBitmap, (softwareBitmap.width * scale).toInt(), (softwareBitmap.height * scale).toInt(), true)
            } catch (e: Exception) {
                softwareBitmap
            }
        } else {
            softwareBitmap
        }

        try {
            val response = model!!.generateContent(
                content {
                    image(scaledBitmap)
                    text(promptText)
                }
            )
            
            val jsonText = response.text?.replace("```json", "")?.replace("```", "")?.trim() ?: return@withContext null
            val json = JSONObject(jsonText)
            
            VisualAxes(
                faceShape = json.optDouble("faceShape", 0.5).toFloat(),
                jawWidthRatio = json.optDouble("jawWidthRatio", 0.5).toFloat(),
                jawAngle = json.optDouble("jawAngle", 0.5).toFloat(),
                chinLengthRatio = json.optDouble("chinLengthRatio", 0.5).toFloat(),
                chinSharpness = json.optDouble("chinSharpness", 0.5).toFloat(),
                foreheadWidthRatio = json.optDouble("foreheadWidthRatio", 0.5).toFloat(),
                foreheadHeightRatio = json.optDouble("foreheadHeightRatio", 0.5).toFloat(),
                cheekboneWidthRatio = json.optDouble("cheekboneWidthRatio", 0.5).toFloat(),
                faceHeightRatio = json.optDouble("faceHeightRatio", 0.5).toFloat(),
                eyeSizeRatio = json.optDouble("eyeSizeRatio", 0.5).toFloat(),
                eyeSpacingRatio = json.optDouble("eyeSpacingRatio", 0.5).toFloat(),
                eyeTilt = json.optDouble("eyeTilt", 0.5).toFloat(),
                eyeRoundness = json.optDouble("eyeRoundness", 0.5).toFloat(),
                eyebrowThickness = json.optDouble("eyebrowThickness", 0.5).toFloat(),
                eyebrowCurve = json.optDouble("eyebrowCurve", 0.5).toFloat(),
                noseLengthRatio = json.optDouble("noseLengthRatio", 0.5).toFloat(),
                noseWidthRatio = json.optDouble("noseWidthRatio", 0.5).toFloat(),
                mouthWidthRatio = json.optDouble("mouthWidthRatio", 0.5).toFloat(),
                lipThickness = json.optDouble("lipThickness", 0.5).toFloat(),
                earSizeRatio = json.optDouble("earSizeRatio", 0.5).toFloat(),
                hairlineHeight = json.optDouble("hairlineHeight", 0.5).toFloat(),
                neckWidthRatio = json.optDouble("neckWidthRatio", 0.5).toFloat(),
                symmetry = json.optDouble("symmetry", 0.5).toFloat(),
                expressionNeutrality = json.optDouble("expressionNeutrality", 0.5).toFloat(),
                stylizationIndex = json.optDouble("stylizationIndex", 0.3).toFloat()
            )
        } catch (e: Throwable) {
            e.printStackTrace()
            null
        }
    }
}
