package com.example.bilingualbuddy.data.repository

import com.example.bilingualbuddy.api.AIService
import com.example.bilingualbuddy.model.Answer
import com.example.bilingualbuddy.util.Result
import javax.inject.Inject

class AIRepositoryImpl @Inject constructor(
    private val aiService: AIService
) : AIRepository {
    
    override suspend fun getAnswer(question: String): Result<Answer> {
        return try {
            if (question.isBlank()) {
                return Result.Error("질문을 입력해주세요")
            }
            val answer = aiService.getAnswer(question)
            Result.Success(answer)
        } catch (e: Exception) {
            Result.Error(e.message ?: "알 수 없는 오류가 발생했습니다")
        }
    }
    
    override suspend fun processImage(imageUri: String): Result<Answer> {
        return try {
            // 이미지 URI는 OCR을 통해 텍스트로 변환된 후 질문으로 사용됨
            // 실제 이미지 처리는 화면 레벨에서 OCR 수행 후 getAnswer 호출
            Result.Error("이미지 처리는 OCR을 통해 텍스트 추출 후 질문으로 변환해주세요")
        } catch (e: Exception) {
            Result.Error(e.message ?: "이미지 처리 중 오류가 발생했습니다")
        }
    }
}

