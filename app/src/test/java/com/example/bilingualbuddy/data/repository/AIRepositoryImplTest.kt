package com.example.bilingualbuddy.data.repository

import com.example.bilingualbuddy.api.AIService
import com.example.bilingualbuddy.model.Answer
import com.example.bilingualbuddy.util.Result
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class AIRepositoryImplTest {
    
    private lateinit var mockAIService: AIService
    private lateinit var repository: AIRepositoryImpl
    
    @Before
    fun setup() {
        mockAIService = mock()
        repository = AIRepositoryImpl(mockAIService)
    }
    
    @Test
    fun `getAnswer returns Success when service succeeds`() = runTest {
        // Given
        val question = "테스트 질문"
        val expectedAnswer = Answer(
            vietnameseSummary = "테스트 베트남어",
            koreanExplanation = "테스트 한국어",
            pronunciation = "테스트 발음"
        )
        whenever(mockAIService.getAnswer(question)).thenReturn(expectedAnswer)
        
        // When
        val result = repository.getAnswer(question)
        
        // Then
        assertTrue(result is Result.Success)
        assertEquals(expectedAnswer, (result as Result.Success).data)
    }
    
    @Test
    fun `getAnswer returns Error when service throws exception`() = runTest {
        // Given
        val question = "테스트 질문"
        val errorMessage = "API 오류"
        whenever(mockAIService.getAnswer(question)).thenThrow(Exception(errorMessage))
        
        // When
        val result = repository.getAnswer(question)
        
        // Then
        assertTrue(result is Result.Error)
        assertEquals(errorMessage, (result as Result.Error).message)
    }
    
    @Test
    fun `processImage returns Success when service succeeds`() = runTest {
        // Given
        val imageUri = "test://image"
        val expectedAnswer = Answer(
            vietnameseSummary = "이미지 분석 결과",
            koreanExplanation = "이미지 분석 결과",
            pronunciation = "발음"
        )
        whenever(mockAIService.processImage(imageUri)).thenReturn(expectedAnswer)
        
        // When
        val result = repository.processImage(imageUri)
        
        // Then
        assertTrue(result is Result.Success)
        assertEquals(expectedAnswer, (result as Result.Success).data)
    }
}

