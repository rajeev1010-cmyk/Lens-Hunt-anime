import re

with open("app/src/main/java/com/example/ui/screens/DebugScreen.kt", "r") as f:
    content = f.read()

# I see what happened. The old metrics were duplicated in my last replacement, or I didn't actually replace them correctly. 
# Let's just find the scrollable column that renders these and replace all MetricBar children at once.

start_str = "MetricBar(\"Confidence\", match.score, match.score)"
end_str = "}" # The end of the Column

idx_start = content.find(start_str)

if idx_start != -1:
    idx_end = content.find("}", idx_start)
    if idx_end != -1:
        # Just write what it should be
        correct_metrics = """MetricBar("Confidence", match.score, match.score)
        Spacer(modifier = Modifier.height(16.dp))
        
        Text("Detailed Visual Profile Axes", style = MaterialTheme.typography.titleMedium, color = Color.White)
        Spacer(modifier = Modifier.height(8.dp))
        
        MetricBar("Face Shape", match.profile.faceShape, match.character.profile.faceShape)
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
        
        # Replace the contents of the column inside the LazyColumn item
        new_content = content[:idx_start] + correct_metrics + "\n    }\n}"
        
        with open("app/src/main/java/com/example/ui/screens/DebugScreen.kt", "w") as f:
            f.write(new_content)

