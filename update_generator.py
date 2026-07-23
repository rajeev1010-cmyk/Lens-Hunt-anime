import re

with open("app/src/main/java/com/example/util/ShareCardGenerator.kt", "r") as f:
    content = f.read()

print("Current function definition:")
print(re.search(r'suspend fun generateAndShare.*?\)\s*\{', content, re.DOTALL).group(0))

