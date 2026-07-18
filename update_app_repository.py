import re

with open("app/src/main/java/com/example/data/repository/AppRepository.kt", "r") as f:
    content = f.read()

stylize_func = """
    private fun stylize(humanValue: Float, exaggeration: Float): Float {
        val centered = humanValue - 0.5f
        val pushed = centered * (1f + exaggeration)
        return (pushed + 0.5f).coerceIn(0.02f, 0.98f)
    }

    private fun stylizeAxes(axes: VisualAxes): VisualAxes {
        val exaggerationByField = mapOf(
            "eyeSizeRatio" to 0.9f, "eyeRoundness" to 0.7f, "eyeTilt" to 0.5f,
            "jawWidthRatio" to 0.6f, "chinSharpness" to 0.6f, "faceShape" to 0.5f,
            "noseWidthRatio" to 0.2f, "noseLengthRatio" to 0.2f, "earSizeRatio" to 0.15f
        )
        fun ex(field: String): Float = exaggerationByField[field] ?: 0.35f
        
        return axes.copy(
            faceShape = stylize(axes.faceShape, ex("faceShape")),
            jawWidthRatio = stylize(axes.jawWidthRatio, ex("jawWidthRatio")),
            jawAngle = stylize(axes.jawAngle, ex("jawAngle")),
            chinLengthRatio = stylize(axes.chinLengthRatio, ex("chinLengthRatio")),
            chinSharpness = stylize(axes.chinSharpness, ex("chinSharpness")),
            foreheadWidthRatio = stylize(axes.foreheadWidthRatio, ex("foreheadWidthRatio")),
            foreheadHeightRatio = stylize(axes.foreheadHeightRatio, ex("foreheadHeightRatio")),
            cheekboneWidthRatio = stylize(axes.cheekboneWidthRatio, ex("cheekboneWidthRatio")),
            faceHeightRatio = stylize(axes.faceHeightRatio, ex("faceHeightRatio")),
            eyeSizeRatio = stylize(axes.eyeSizeRatio, ex("eyeSizeRatio")),
            eyeSpacingRatio = stylize(axes.eyeSpacingRatio, ex("eyeSpacingRatio")),
            eyeTilt = stylize(axes.eyeTilt, ex("eyeTilt")),
            eyeRoundness = stylize(axes.eyeRoundness, ex("eyeRoundness")),
            eyebrowThickness = stylize(axes.eyebrowThickness, ex("eyebrowThickness")),
            eyebrowCurve = stylize(axes.eyebrowCurve, ex("eyebrowCurve")),
            noseLengthRatio = stylize(axes.noseLengthRatio, ex("noseLengthRatio")),
            noseWidthRatio = stylize(axes.noseWidthRatio, ex("noseWidthRatio")),
            mouthWidthRatio = stylize(axes.mouthWidthRatio, ex("mouthWidthRatio")),
            lipThickness = stylize(axes.lipThickness, ex("lipThickness")),
            earSizeRatio = stylize(axes.earSizeRatio, ex("earSizeRatio")),
            hairlineHeight = stylize(axes.hairlineHeight, ex("hairlineHeight")),
            neckWidthRatio = stylize(axes.neckWidthRatio, ex("neckWidthRatio")),
            symmetry = axes.symmetry, 
            expressionNeutrality = axes.expressionNeutrality,
            stylizationIndex = axes.stylizationIndex
        )
    }
"""

content = content.replace("class AppRepository(private val dao: AppDao) {", "class AppRepository(private val dao: AppDao) {\n" + stylize_func)

content = content.replace(
    "val rawResults = candidates.map { character ->",
    "val stylizedAxes = stylizeAxes(axes)\n        val rawResults = candidates.map { character ->"
)

content = content.replace(
    "SimilarityCalculator.calculateWithDetails(axes.toArray(), character.profile.toArray())",
    "SimilarityCalculator.calculateWithDetails(stylizedAxes.toArray(), character.profile.toArray())"
)

with open("app/src/main/java/com/example/data/repository/AppRepository.kt", "w") as f:
    f.write(content)
