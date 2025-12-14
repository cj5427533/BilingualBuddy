package com.example.bilingualbuddy.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bilingualbuddy.data.repository.AIRepository
import com.example.bilingualbuddy.model.Answer
import com.example.bilingualbuddy.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuestionViewModel @Inject constructor(
    private val aiRepository: AIRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow<QuestionUiState>(QuestionUiState.Idle)
    val uiState: StateFlow<QuestionUiState> = _uiState.asStateFlow()
    
    fun askQuestion(question: String) {
        if (question.isBlank()) {
            _uiState.value = QuestionUiState.Error("질문을 입력해주세요")
            return
        }
        
        viewModelScope.launch {
            _uiState.value = QuestionUiState.Loading
            when (val result = aiRepository.getAnswer(question)) {
                is Result.Success -> {
                    _uiState.value = QuestionUiState.Success(result.data)
                }
                is Result.Error -> {
                    _uiState.value = QuestionUiState.Error(result.message)
                }
                is Result.Loading -> {
                    _uiState.value = QuestionUiState.Loading
                }
            }
        }
    }
    
    fun processImage(imageUri: String) {
        viewModelScope.launch {
            _uiState.value = QuestionUiState.Loading
            when (val result = aiRepository.processImage(imageUri)) {
                is Result.Success -> {
                    _uiState.value = QuestionUiState.Success(result.data)
                }
                is Result.Error -> {
                    _uiState.value = QuestionUiState.Error(result.message)
                }
                is Result.Loading -> {
                    _uiState.value = QuestionUiState.Loading
                }
            }
        }
    }
    
    fun resetState() {
        _uiState.value = QuestionUiState.Idle
    }
    
    fun setError(message: String) {
        _uiState.value = QuestionUiState.Error(message)
    }
}

sealed class QuestionUiState {
    object Idle : QuestionUiState()
    object Loading : QuestionUiState()
    data class Success(val answer: Answer) : QuestionUiState()
    data class Error(val message: String) : QuestionUiState()
}

