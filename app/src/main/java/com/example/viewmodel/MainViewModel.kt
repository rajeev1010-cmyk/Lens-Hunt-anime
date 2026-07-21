package com.example.viewmodel

import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.data.local.AppDatabase
import com.example.data.model.VisualAxes
import com.example.data.repository.AppRepository
import com.example.domain.analyzer.FaceAnalysisResult
import com.example.domain.analyzer.FaceAnalyzer
import com.example.domain.matcher.MatchResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val db = Room.databaseBuilder(
        application,
        AppDatabase::class.java,
        "lens_hunt_db"
    ).fallbackToDestructiveMigration().build()

    private val repository = AppRepository(db.appDao())
    private val faceAnalyzer = FaceAnalyzer()

    private val _topMatches = MutableStateFlow<List<MatchResult>>(emptyList())
    val topMatches: StateFlow<List<MatchResult>> = _topMatches.asStateFlow()

    private val _matchDebugInfo = MutableStateFlow<com.example.domain.matcher.MatchDebugInfo?>(null)
    val matchDebugInfo: StateFlow<com.example.domain.matcher.MatchDebugInfo?> = _matchDebugInfo.asStateFlow()

    private val _isAnalyzing = MutableStateFlow(false)
    val isAnalyzing: StateFlow<Boolean> = _isAnalyzing.asStateFlow()

    private val _userProfile = MutableStateFlow<VisualAxes?>(null)
    val userProfile: StateFlow<VisualAxes?> = _userProfile.asStateFlow()

    private val _faceResult = MutableStateFlow<FaceAnalysisResult?>(null)
    val faceResult: StateFlow<FaceAnalysisResult?> = _faceResult.asStateFlow()

    private val _userSelfie = MutableStateFlow<android.graphics.Bitmap?>(null)
    val userSelfie: StateFlow<android.graphics.Bitmap?> = _userSelfie.asStateFlow()

    private val _genderFilter = MutableStateFlow("AUTO")
    val genderFilter: StateFlow<String> = _genderFilter.asStateFlow()

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
        _userArchetype.value = getRomajiArchetype(bestArchetype)

        // 2. Generate anime first name
        val name = _firstName.value.trim().uppercase()
        val firstLetter = if (name.isNotEmpty()) name.first() else 'A'
        val letterSyllable = getFirstLetterSyllable(firstLetter)
        val monthSyllable = getMonthSyllable(_dobMonth.value)
        val daySyllable = getDobSyllable(_dobDay.value)

        _userAnimeFirstName.value = daySyllable + monthSyllable + letterSyllable
    }

    
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


    fun setGenderFilter(filter: String) {
        _genderFilter.value = filter
    }

    val history = repository.history

    private var lastMatchTime = 0L

    private val _allCharacters = MutableStateFlow<List<com.example.data.local.CharacterEntity>>(emptyList())
    val allCharacters: StateFlow<List<com.example.data.local.CharacterEntity>> = _allCharacters.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.seedDatabaseIfEmpty()
            repository.allCharacters.collect { chars ->
                _allCharacters.value = chars
            }
        }
    }

    fun saveSelfie(bitmap: android.graphics.Bitmap) {
        _userSelfie.value = bitmap
    }

    fun onFaceAnalyzed(result: FaceAnalysisResult?) {
        _faceResult.value = result
        _userProfile.value = result?.axes
        
        if (result == null) {
            _topMatches.value = emptyList()
            return
        }

        val currentTime = System.currentTimeMillis()
        if (currentTime - lastMatchTime < 200) {
            return
        }
        lastMatchTime = currentTime

        viewModelScope.launch(Dispatchers.IO) {
            _isAnalyzing.value = true
            val (matches, debugInfo) = repository.findMatches(
                result.axes,
                result.visualPresentation,
                result.presentationConfidence,
                _genderFilter.value
            )
            _topMatches.value = matches
            generateUserIdentity(matches)
            _matchDebugInfo.value = debugInfo
            _isAnalyzing.value = false
        }
    }
    
    fun analyzePhoto(context: Context, uri: Uri, onComplete: (Boolean) -> Unit) {
        _isAnalyzing.value = true
        faceAnalyzer.analyzeUri(context, uri) { result, bitmap ->
            if (result != null && bitmap != null) {
                saveSelfie(bitmap)
                onFaceAnalyzed(result)
                saveCurrentMatch()
                onComplete(true)
            } else {
                _isAnalyzing.value = false
                onComplete(false)
            }
        }
    }

    fun saveCurrentMatch() {
        val matches = _topMatches.value
        val profile = _userProfile.value
        if (matches.isNotEmpty() && profile != null) {
            viewModelScope.launch(Dispatchers.IO) {
                repository.saveHistory(matches.first().character.id, matches.first().score, profile)
            }
        }
    }
}
