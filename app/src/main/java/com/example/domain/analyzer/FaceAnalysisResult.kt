package com.example.domain.analyzer

import android.graphics.Rect
import com.example.data.model.VisualAxes

data class FaceAnalysisResult(
    val axes: VisualAxes,
    val boundingBox: Rect,
    val imageWidth: Int,
    val imageHeight: Int,
    val rotationDegrees: Int
)
