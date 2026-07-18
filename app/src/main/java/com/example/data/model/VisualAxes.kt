package com.example.data.model

import androidx.room.Embedded

data class VisualAxes(
    val faceShape: Float = 0.5f,
    val jawWidthRatio: Float = 0.5f,
    val jawAngle: Float = 0.5f,
    val chinLengthRatio: Float = 0.5f,
    val chinSharpness: Float = 0.5f,
    val foreheadWidthRatio: Float = 0.5f,
    val foreheadHeightRatio: Float = 0.5f,
    val cheekboneWidthRatio: Float = 0.5f,
    val faceHeightRatio: Float = 0.5f,
    val eyeSizeRatio: Float = 0.5f,
    val eyeSpacingRatio: Float = 0.5f,
    val eyeTilt: Float = 0.5f,
    val eyeRoundness: Float = 0.5f,
    val eyebrowThickness: Float = 0.5f,
    val eyebrowCurve: Float = 0.5f,
    val noseLengthRatio: Float = 0.5f,
    val noseWidthRatio: Float = 0.5f,
    val mouthWidthRatio: Float = 0.5f,
    val lipThickness: Float = 0.5f,
    val earSizeRatio: Float = 0.5f,
    val hairlineHeight: Float = 0.5f,
    val neckWidthRatio: Float = 0.5f,
    val symmetry: Float = 0.5f,
    val expressionNeutrality: Float = 0.5f,
    val stylizationIndex: Float = 0.5f
) {
    fun toArray(): FloatArray = floatArrayOf(
        faceShape, jawWidthRatio, jawAngle, chinLengthRatio, chinSharpness,
        foreheadWidthRatio, foreheadHeightRatio, cheekboneWidthRatio, faceHeightRatio,
        eyeSizeRatio, eyeSpacingRatio, eyeTilt, eyeRoundness,
        eyebrowThickness, eyebrowCurve,
        noseLengthRatio, noseWidthRatio,
        mouthWidthRatio, lipThickness,
        earSizeRatio, hairlineHeight, neckWidthRatio,
        symmetry, expressionNeutrality
    )
}
