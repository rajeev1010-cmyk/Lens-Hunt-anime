import re

with open("app/src/main/java/com/example/domain/analyzer/FaceAnalyzer.kt", "r") as f:
    content = f.read()

content = content.replace("val score = (axes.jawSharpness + axes.browWeight + axes.angularity) / 3f", "val score = (axes.chinSharpness + axes.eyebrowThickness + axes.faceShape) / 3f")

content = content.replace("""        return VisualAxes(
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
        )""", """        return VisualAxes(
            faceShape = 0.5f,
            jawWidthRatio = 0.5f,
            jawAngle = 0.5f,
            chinLengthRatio = 0.5f,
            chinSharpness = jawSharpness,
            foreheadWidthRatio = 0.5f,
            foreheadHeightRatio = 0.5f,
            cheekboneWidthRatio = 0.5f,
            faceHeightRatio = faceLength,
            eyeSizeRatio = 0.5f,
            eyeSpacingRatio = 0.5f,
            eyeTilt = 0.5f,
            eyeRoundness = 0.5f,
            eyebrowThickness = browWeight,
            eyebrowCurve = 0.5f,
            noseLengthRatio = 0.5f,
            noseWidthRatio = 0.5f,
            mouthWidthRatio = 0.5f,
            lipThickness = 0.5f,
            earSizeRatio = 0.5f,
            hairlineHeight = 0.5f,
            neckWidthRatio = 0.5f,
            symmetry = symmetry,
            expressionNeutrality = expressionNeutrality,
            stylizationIndex = 0.5f
        )""")

with open("app/src/main/java/com/example/domain/analyzer/FaceAnalyzer.kt", "w") as f:
    f.write(content)
