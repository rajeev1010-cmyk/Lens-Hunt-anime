import re

with open("app/src/main/java/com/example/viewmodel/MainViewModel.kt", "r") as f:
    content = f.read()

mapping_code = """
    private fun getRomajiArchetype(englishArchetype: String): String {
        return when (englishArchetype) {
            "Philosopher" -> "Tetsugaku"
            "Technocrat" -> "Gijutsu"
            "Democrat" -> "Minshū"
            "Aristocrat" -> "Kizoku"
            "Bureaucrat" -> "Kanryō"
            "Strategist" -> "Senryaku"
            "Architect" -> "Kenchiku"
            "Visionary" -> "Riso"
            "Judge" -> "Sabaki"
            "Diplomat" -> "Gaikō"
            "Sage" -> "Kenja"
            "Reformer" -> "Kaikaku"
            "Chancellor" -> "Sōsai"
            "Oracle" -> "Yogen"
            "Historian" -> "Rekishi"
            "Custodian" -> "Hozon"
            "Guardian" -> "Mamori"
            "Survivor" -> "Seizon"
            "Builder" -> "Kensetsu"
            "Explorer" -> "Tansa"
            "Messenger" -> "Shisha"
            "Navigator" -> "Kōro"
            "Artisan" -> "Shokunin"
            "Healer" -> "Iyashi"
            "Inventor" -> "Hatsumei"
            "Pathfinder" -> "Kaitaku"
            "Warden" -> "Banri"
            "Scout" -> "Teisatsu"
            "Farmer" -> "Nōgyō"
            "Ranger" -> "Yūei"
            "Storyteller" -> "Monogatari"
            "Craftsman" -> "Takumi"
            else -> englishArchetype
        }
    }
"""

if "getRomajiArchetype" not in content:
    content = content.replace("private fun getFirstLetterSyllable", mapping_code + "\n    private fun getFirstLetterSyllable")

content = content.replace('_userArchetype.value = bestArchetype', '_userArchetype.value = getRomajiArchetype(bestArchetype)')

with open("app/src/main/java/com/example/viewmodel/MainViewModel.kt", "w") as f:
    f.write(content)
