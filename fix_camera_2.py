import re
with open("app/src/main/java/com/example/ui/screens/CameraScreen.kt", "r") as f:
    lines = f.readlines()

new_lines = []
for i, line in enumerate(lines):
    if "if (isAnalyzing) {" in line and i < 70:
        continue
    if "Box(" in line and 58 <= i <= 62:
        continue
    if "modifier =" in line and 59 <= i <= 63:
        continue
    if ".fillMaxSize()" in line and 60 <= i <= 64:
        continue
    if ".background" in line and 61 <= i <= 65:
        continue
    if "contentAlignment =" in line and 62 <= i <= 66:
        continue
    if "Column(" in line and 63 <= i <= 67:
        continue
    if "CircularProgressIndicator" in line and 64 <= i <= 68:
        continue
    if "Spacer(" in line and 65 <= i <= 69:
        continue
    if "Text(" in line and 66 <= i <= 70:
        continue
    if "}" in line and 67 <= i <= 73:
        continue
    
    new_lines.append(line)

# Let's just do a simpler string replace to remove the first block
with open("app/src/main/java/com/example/ui/screens/CameraScreen.kt", "r") as f:
    content = f.read()

bad_block = """        if (isAnalyzing) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.6f)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(color = Color.White)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Analyzing with Gemini...", color = Color.White, style = MaterialTheme.typography.titleMedium)
                }
            }
        }"""

content = content.replace(bad_block, "", 1) # Only replace the first occurrence!

with open("app/src/main/java/com/example/ui/screens/CameraScreen.kt", "w") as f:
    f.write(content)
