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
import com.example.domain.analyzer.GeminiService
import android.graphics.Bitmap
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

    
    private val geminiService = GeminiService()

    fun captureAndAnalyze(bitmap: Bitmap, onComplete: () -> Unit) {
        _isAnalyzing.value = true
        _userSelfie.value = bitmap
        
        viewModelScope.launch(Dispatchers.Main) {
            val axes = geminiService.analyzeFace(bitmap)
            
            if (axes != null) {
                _userProfile.value = axes
                val (matches, debugInfo) = repository.findMatches(
                    axes,
                    _faceResult.value?.visualPresentation ?: "unknown",
                    _faceResult.value?.presentationConfidence ?: 0.5f,
                    _genderFilter.value
                )
                _topMatches.value = matches
                _matchDebugInfo.value = debugInfo
                saveCurrentMatch()
            }
            
            _isAnalyzing.value = false
            onComplete()
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
