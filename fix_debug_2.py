import re

with open("app/src/main/java/com/example/ui/screens/DebugScreen.kt", "r") as f:
    content = f.read()

# Replace any remaining old metric references
old_metrics = [
    'MetricBar("Face Length", match.profile.faceLength, match.character.profile.faceLength)',
    'MetricBar("Jaw Sharpness", match.profile.jawSharpness, match.character.profile.jawSharpness)',
    'MetricBar("Eye Narrowness", match.profile.eyeNarrowness, match.character.profile.eyeNarrowness)',
    'MetricBar("Brow Weight", match.profile.browWeight, match.character.profile.browWeight)',
    'MetricBar("Hair Darkness", match.profile.hairDarkness, match.character.profile.hairDarkness)',
    'MetricBar("Hair Volume", match.profile.hairVolume, match.character.profile.hairVolume)',
    'MetricBar("Contrast", match.profile.contrast, match.character.profile.contrast)',
    'MetricBar("Angularity", match.profile.angularity, match.character.profile.angularity)',
    'MetricBar("Glasses", match.profile.glasses, match.character.profile.glasses)',
    'MetricBar("Warmth", match.profile.warmth, match.character.profile.warmth)',
]

for m in old_metrics:
    content = content.replace(m, "")

with open("app/src/main/java/com/example/ui/screens/DebugScreen.kt", "w") as f:
    f.write(content)
