package com.example.bilingualbuddy.ui.viewmodel

import com.example.bilingualbuddy.data.repository.AIRepository
import com.example.bilingualbuddy.model.Answer
import com.example.bilingualbuddy.util.Result
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class QuestionViewModelTest {
    
    private lateinit var mockRepository: AIRepository
    private lateinit var viewModel: QuestionViewModel
    
    @Before
    fun setup() {
        mockRepository = mock()
        viewModel = QuestionViewModel(mockRepository)
    }
    
    @Test
    fun `askQuestion with empty question sets Error state`() = runTest {
        // When
        viewModel.askQuestion("")
        
        // Then
        val states = viewModel.uiState.take(2).toList()
        val errorState = states.find { it is QuestionUiState.Error }
        assertNotNull(errorState)
        assertTrue(errorState is QuestionUiState.Error)
        assertEquals("질문을 입력해주세요", (errorState as QuestionUiState.Error).message)
    }
    
    @Test
    fun `askQuestion with valid question sets Success state`() = runTest {
        // Given
        val question = "테스트 질문"
        val answer = Answer(
            vietnameseSummary = "베트남어",
            koreanExplanation = "한국어",
            pronunciation = "발음"
        )
        whenever(mockRepository.getAnswer(question)).thenReturn(Result.Success(answer))
        
        // When
        viewModel.askQuestion(question)
        
        // Then
        val states = viewModel.uiState.take(3).toList()
        val successState = states.find { it is QuestionUiState.Success }
        assertNotNull(successState)
        assertTrue(successState is QuestionUiState.Success)
        assertEquals(answer, (successState as QuestionUiState.Success).answer)
    }
    
    @Test
    fun `askQuestion with repository error sets Error state`() = runTest {
        // Given
        val question = "테스트 질문"
        val errorMessage = "API 오류"
        whenever(mockRepository.getAnswer(question)).thenReturn(Result.Error(errorMessage))
        
        // When
        viewModel.askQuestion(question)
        
        // Then
        val states = viewModel.uiState.take(3).toList()
        val errorState = states.find { it is QuestionUiState.Error }
        assertNotNull(errorState)
        assertTrue(errorState is QuestionUiState.Error)
        assertEquals(errorMessage, (errorState as QuestionUiState.Error).message)
    }
    
    @Test
    fun `resetState sets Idle state`() = runTest {
        // When
        viewModel.resetState()
        
        // Then
        val state = viewModel.uiState.take(1).toList().first()
        assertTrue(state is QuestionUiState.Idle)
    }
}

