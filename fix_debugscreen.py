import re

with open("app/src/main/java/com/example/ui/screens/DebugScreen.kt", "r") as f:
    content = f.read()

replacement = """        MetricBar("Face Shape", match.profile.faceShape, match.character.profile.faceShape)
        MetricBar("Jaw Width", match.profile.jawWidthRatio, match.character.profile.jawWidthRatio)
        MetricBar("Jaw Angle", match.profile.jawAngle, match.character.profile.jawAngle)
        MetricBar("Chin Length", match.profile.chinLengthRatio, match.character.profile.chinLengthRatio)
        MetricBar("Chin Sharpness", match.profile.chinSharpness, match.character.profile.chinSharpness)
        MetricBar("Face Height", match.profile.faceHeightRatio, match.character.profile.faceHeightRatio)
        MetricBar("Eye Size", match.profile.eyeSizeRatio, match.character.profile.eyeSizeRatio)
        MetricBar("Eye Spacing", match.profile.eyeSpacingRatio, match.character.profile.eyeSpacingRatio)
        MetricBar("Eye Tilt", match.profile.eyeTilt, match.character.profile.eyeTilt)
        MetricBar("Eye Roundness", match.profile.eyeRoundness, match.character.profile.eyeRoundness)
        MetricBar("Eyebrow Thickness", match.profile.eyebrowThickness, match.character.profile.eyebrowThickness)
        MetricBar("Eyebrow Curve", match.profile.eyebrowCurve, match.character.profile.eyebrowCurve)
        MetricBar("Symmetry", match.profile.symmetry, match.character.profile.symmetry)
        MetricBar("Expression Neutrality", match.profile.expressionNeutrality, match.character.profile.expressionNeutrality)"""

# Look for the old metrics block and replace it
content = re.sub(r'MetricBar\("Face Length".*?MetricBar\("Warmth"[^\n]*\n', replacement + "\n", content, flags=re.DOTALL)

with open("app/src/main/java/com/example/ui/screens/DebugScreen.kt", "w") as f:
    f.write(content)
