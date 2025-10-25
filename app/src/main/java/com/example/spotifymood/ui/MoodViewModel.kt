package com.example.spotifymood.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spotifymood.data.MoodSnapshot
import com.example.spotifymood.data.SpotifyMoodRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MoodViewModel(
    private val repository: SpotifyMoodRepository
) : ViewModel() {

    private val _moodState = MutableStateFlow<MoodSnapshot?>(null)
    val moodState: StateFlow<MoodSnapshot?> = _moodState.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    fun refresh(accessToken: String) {
        if (accessToken.isBlank()) return
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val snapshot = repository.loadCurrentMood(accessToken)
                _moodState.value = snapshot
            } catch (error: Exception) {
                _errorMessage.value = error.localizedMessage ?: "Άγνωστο σφάλμα"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
