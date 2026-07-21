import re

with open("app/src/main/java/com/example/ui/screens/ResultsScreen.kt", "r") as f:
    content = f.read()

content = content.replace("ShareCardGenerator.generateAndShare(context, userSelfie!!, topMatch, userAnimeFirstName, userArchetype)", "ShareCardGenerator.generateAndShare(context, userSelfie!!, topMatches, userAnimeFirstName, userArchetype)")

with open("app/src/main/java/com/example/ui/screens/ResultsScreen.kt", "w") as f:
    f.write(content)
