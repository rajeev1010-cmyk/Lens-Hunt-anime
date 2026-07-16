package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.data.local.AppDatabase
import com.example.data.model.VisualAxes
import com.example.data.repository.AppRepository
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
    ).build()

    private val repository = AppRepository(db.appDao())

    private val _topMatches = MutableStateFlow<List<MatchResult>>(emptyList())
    val topMatches: StateFlow<List<MatchResult>> = _topMatches.asStateFlow()

    private val _isAnalyzing = MutableStateFlow(false)
    val isAnalyzing: StateFlow<Boolean> = _isAnalyzing.asStateFlow()

    private val _userProfile = MutableStateFlow<VisualAxes?>(null)
    val userProfile: StateFlow<VisualAxes?> = _userProfile.asStateFlow()

    val history = repository.history

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.seedDatabaseIfEmpty()
        }
    }

    fun onFaceAnalyzed(axes: VisualAxes?) {
        _userProfile.value = axes
        if (axes == null) {
            _topMatches.value = emptyList()
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            _isAnalyzing.value = true
            val matches = repository.findMatches(axes)
            _topMatches.value = matches
            _isAnalyzing.value = false
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
