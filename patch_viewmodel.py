import re

with open("app/src/main/java/com/example/viewmodel/MainViewModel.kt", "r") as f:
    content = f.read()

new_state = """
    private val _firstName = MutableStateFlow("")
    val firstName: StateFlow<String> = _firstName.asStateFlow()

    private val _dobDay = MutableStateFlow(1)
    val dobDay: StateFlow<Int> = _dobDay.asStateFlow()

    private val _dobMonth = MutableStateFlow(1)
    val dobMonth: StateFlow<Int> = _dobMonth.asStateFlow()

    private val _userArchetype = MutableStateFlow("")
    val userArchetype: StateFlow<String> = _userArchetype.asStateFlow()

    private val _userAnimeFirstName = MutableStateFlow("")
    val userAnimeFirstName: StateFlow<String> = _userAnimeFirstName.asStateFlow()

    fun setFirstName(name: String) { _firstName.value = name }
    fun setDobDay(day: Int) { _dobDay.value = day }
    fun setDobMonth(month: Int) { _dobMonth.value = month }

    private fun generateUserIdentity(matches: List<MatchResult>) {
        if (matches.isEmpty()) return
        
        // 1. Calculate archetype based on sum of confidence of top 5 matches
        val top5 = matches.take(5)
        val archetypeScores = mutableMapOf<String, Float>()
        for (match in top5) {
            val arch = match.character.archetype
            if (arch.isNotEmpty()) {
                archetypeScores[arch] = (archetypeScores[arch] ?: 0f) + match.similarityPercentage
            }
        }
        val bestArchetype = archetypeScores.maxByOrNull { it.value }?.key ?: "Unknown"
        _userArchetype.value = bestArchetype

        // 2. Generate anime first name
        val name = _firstName.value.trim().uppercase()
        val firstLetter = if (name.isNotEmpty()) name.first() else 'A'
        val letterSyllable = getFirstLetterSyllable(firstLetter)
        val monthSyllable = getMonthSyllable(_dobMonth.value)
        val daySyllable = getDobSyllable(_dobDay.value)

        _userAnimeFirstName.value = letterSyllable + monthSyllable + daySyllable
    }

    private fun getFirstLetterSyllable(c: Char): String = when(c) {
        'A' -> "Ka"; 'B' -> "Ki"; 'C' -> "Ku"; 'D' -> "Ke"; 'E' -> "Ko"
        'F' -> "Sa"; 'G' -> "Shi"; 'H' -> "Su"; 'I' -> "Se"; 'J' -> "So"
        'K' -> "Ta"; 'L' -> "Chi"; 'M' -> "Tsu"; 'N' -> "Te"; 'O' -> "To"
        'P' -> "Na"; 'Q' -> "Ni"; 'R' -> "Nu"; 'S' -> "Ne"; 'T' -> "No"
        'U' -> "Ha"; 'V' -> "Hi"; 'W' -> "Fu"; 'X' -> "He"; 'Y' -> "Ho"; 'Z' -> "Ra"
        else -> "Ka"
    }

    private fun getMonthSyllable(m: Int): String = when(m) {
        1 -> "A"; 2 -> "I"; 3 -> "U"; 4 -> "E"; 5 -> "O"; 6 -> "Ya"
        7 -> "Yu"; 8 -> "Yo"; 9 -> "Ri"; 10 -> "Ka"; 11 -> "Na"; 12 -> "Mi"
        else -> "A"
    }

    private fun getDobSyllable(d: Int): String = when(d) {
        1 -> "Ka"; 2 -> "Ki"; 3 -> "Ku"; 4 -> "Ke"; 5 -> "Ko"
        6 -> "Sa"; 7 -> "Shi"; 8 -> "Su"; 9 -> "Se"; 10 -> "So"
        11 -> "Ta"; 12 -> "Chi"; 13 -> "Tsu"; 14 -> "Te"; 15 -> "To"
        16 -> "Na"; 17 -> "Ni"; 18 -> "Nu"; 19 -> "Ne"; 20 -> "No"
        21 -> "Ha"; 22 -> "Hi"; 23 -> "Fu"; 24 -> "He"; 25 -> "Ho"
        26 -> "Ma"; 27 -> "Mi"; 28 -> "Mu"; 29 -> "Me"; 30, 31 -> "Ra"
        else -> "Ka"
    }
"""

content = content.replace("val genderFilter: StateFlow<String> = _genderFilter.asStateFlow()", "val genderFilter: StateFlow<String> = _genderFilter.asStateFlow()\n" + new_state)

content = content.replace("_topMatches.value = matches", "_topMatches.value = matches\n            generateUserIdentity(matches)")

with open("app/src/main/java/com/example/viewmodel/MainViewModel.kt", "w") as f:
    f.write(content)
