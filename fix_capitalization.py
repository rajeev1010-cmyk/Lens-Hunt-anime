import re

with open("app/src/main/java/com/example/viewmodel/MainViewModel.kt", "r") as f:
    content = f.read()

content = content.replace(
    "_userAnimeFirstName.value = daySyllable + monthSyllable + letterSyllable",
    "_userAnimeFirstName.value = (daySyllable + monthSyllable + letterSyllable).lowercase().replaceFirstChar { it.uppercase() }"
)

with open("app/src/main/java/com/example/viewmodel/MainViewModel.kt", "w") as f:
    f.write(content)
