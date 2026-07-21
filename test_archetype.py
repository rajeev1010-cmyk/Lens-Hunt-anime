import re

with open("app/src/main/java/com/example/ui/screens/ResultsScreen.kt", "r") as f:
    content = f.read()

print("RESULTS SCREEN TEXT")
print(re.search(r'ShareCardGenerator.generateAndShare.*?\) \{', content, re.DOTALL))
