import re

with open("app/src/main/java/com/example/ui/screens/DebugScreen.kt", "r") as f:
    content = f.read()

# I see the unresolved references are actually in the "Detected Face Features" section which just prints out the axes.
old_axes_print = """                    val axes = faceResult!!.axes
                    Text("faceLength: ${String.format(Locale.US, "%.3f", axes.faceLength)}")
                    Text("jawSharpness: ${String.format(Locale.US, "%.3f", axes.jawSharpness)}")
                    Text("eyeNarrowness: ${String.format(Locale.US, "%.3f", axes.eyeNarrowness)}")
                    Text("browWeight: ${String.format(Locale.US, "%.3f", axes.browWeight)}")
                    Text("hairDarkness: ${String.format(Locale.US, "%.3f", axes.hairDarkness)}")
                    Text("hairVolume: ${String.format(Locale.US, "%.3f", axes.hairVolume)}")
                    Text("expressionNeutrality: ${String.format(Locale.US, "%.3f", axes.expressionNeutrality)}")
                    Text("symmetry: ${String.format(Locale.US, "%.3f", axes.symmetry)}")
                    Text("contrast: ${String.format(Locale.US, "%.3f", axes.contrast)}")
                    Text("angularity: ${String.format(Locale.US, "%.3f", axes.angularity)}")
                    Text("glasses: ${String.format(Locale.US, "%.3f", axes.glasses)}")
                    Text("warmth: ${String.format(Locale.US, "%.3f", axes.warmth)}")"""

new_axes_print = """                    val axes = faceResult!!.axes
                    Text("faceShape: ${String.format(Locale.US, "%.3f", axes.faceShape)}")
                    Text("jawWidthRatio: ${String.format(Locale.US, "%.3f", axes.jawWidthRatio)}")
                    Text("jawAngle: ${String.format(Locale.US, "%.3f", axes.jawAngle)}")
                    Text("chinLengthRatio: ${String.format(Locale.US, "%.3f", axes.chinLengthRatio)}")
                    Text("chinSharpness: ${String.format(Locale.US, "%.3f", axes.chinSharpness)}")
                    Text("foreheadWidthRatio: ${String.format(Locale.US, "%.3f", axes.foreheadWidthRatio)}")
                    Text("foreheadHeightRatio: ${String.format(Locale.US, "%.3f", axes.foreheadHeightRatio)}")
                    Text("cheekboneWidthRatio: ${String.format(Locale.US, "%.3f", axes.cheekboneWidthRatio)}")
                    Text("faceHeightRatio: ${String.format(Locale.US, "%.3f", axes.faceHeightRatio)}")
                    Text("eyeSizeRatio: ${String.format(Locale.US, "%.3f", axes.eyeSizeRatio)}")
                    Text("eyeSpacingRatio: ${String.format(Locale.US, "%.3f", axes.eyeSpacingRatio)}")
                    Text("eyeTilt: ${String.format(Locale.US, "%.3f", axes.eyeTilt)}")
                    Text("eyeRoundness: ${String.format(Locale.US, "%.3f", axes.eyeRoundness)}")
                    Text("eyebrowThickness: ${String.format(Locale.US, "%.3f", axes.eyebrowThickness)}")
                    Text("eyebrowCurve: ${String.format(Locale.US, "%.3f", axes.eyebrowCurve)}")
                    Text("noseLengthRatio: ${String.format(Locale.US, "%.3f", axes.noseLengthRatio)}")
                    Text("noseWidthRatio: ${String.format(Locale.US, "%.3f", axes.noseWidthRatio)}")
                    Text("mouthWidthRatio: ${String.format(Locale.US, "%.3f", axes.mouthWidthRatio)}")
                    Text("lipThickness: ${String.format(Locale.US, "%.3f", axes.lipThickness)}")
                    Text("earSizeRatio: ${String.format(Locale.US, "%.3f", axes.earSizeRatio)}")
                    Text("hairlineHeight: ${String.format(Locale.US, "%.3f", axes.hairlineHeight)}")
                    Text("neckWidthRatio: ${String.format(Locale.US, "%.3f", axes.neckWidthRatio)}")
                    Text("symmetry: ${String.format(Locale.US, "%.3f", axes.symmetry)}")
                    Text("expressionNeutrality: ${String.format(Locale.US, "%.3f", axes.expressionNeutrality)}")
                    Text("stylizationIndex: ${String.format(Locale.US, "%.3f", axes.stylizationIndex)}")"""

content = content.replace(old_axes_print, new_axes_print)

with open("app/src/main/java/com/example/ui/screens/DebugScreen.kt", "w") as f:
    f.write(content)
