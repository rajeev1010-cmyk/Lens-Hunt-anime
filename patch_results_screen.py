import re

with open("app/src/main/java/com/example/ui/screens/ResultsScreen.kt", "r") as f:
    content = f.read()

content = content.replace("val topMatches by viewModel.topMatches.collectAsState()", "val topMatches by viewModel.topMatches.collectAsState()\n    val userAnimeFirstName by viewModel.userAnimeFirstName.collectAsState()\n    val userArchetype by viewModel.userArchetype.collectAsState()")

content = content.replace("ShareCardGenerator.generateAndShare(context, userSelfie!!, topMatch)", "ShareCardGenerator.generateAndShare(context, userSelfie!!, topMatch, userAnimeFirstName, userArchetype)")

with open("app/src/main/java/com/example/ui/screens/ResultsScreen.kt", "w") as f:
    f.write(content)
