package com.example.bilingualbuddy.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bilingualbuddy.data.repository.TranslateRepository
import com.example.bilingualbuddy.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TranslateViewModel @Inject constructor(
    private val translateRepository: TranslateRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow<TranslateUiState>(TranslateUiState.Idle)
    val uiState: StateFlow<TranslateUiState> = _uiState.asStateFlow()
    
    fun translate(text: String, sourceLang: String, targetLang: String) {
        if (text.isBlank()) {
            _uiState.value = TranslateUiState.Error("번역할 텍스트를 입력해주세요")
            return
        }
        
        viewModelScope.launch {
            _uiState.value = TranslateUiState.Loading
            when (val result = translateRepository.translate(text, sourceLang, targetLang)) {
                is Result.Success -> {
                    _uiState.value = TranslateUiState.Success(result.data)
                }
                is Result.Error -> {
                    _uiState.value = TranslateUiState.Error(result.message)
                }
                is Result.Loading -> {
                    _uiState.value = TranslateUiState.Loading
                }
            }
        }
    }
    
    fun resetState() {
        _uiState.value = TranslateUiState.Idle
    }
}

sealed class TranslateUiState {
    object Idle : TranslateUiState()
    object Loading : TranslateUiState()
    data class Success(val translatedText: String) : TranslateUiState()
    data class Error(val message: String) : TranslateUiState()
}

