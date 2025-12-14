package com.example.bilingualbuddy.data.repository

import com.example.bilingualbuddy.api.TranslateApi
import com.example.bilingualbuddy.util.Result
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

class TranslateRepositoryImpl @Inject constructor() : TranslateRepository {
    
    override suspend fun translate(text: String, sourceLang: String, targetLang: String): Result<String> {
        return try {
            suspendCancellableCoroutine { continuation ->
                TranslateApi.translate(text, sourceLang, targetLang) { translatedText ->
                    if (translatedText.contains("오류") || translatedText.contains("실패")) {
                        continuation.resume(Result.Error(translatedText))
                    } else {
                        continuation.resume(Result.Success(translatedText))
                    }
                }
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "번역 중 오류가 발생했습니다")
        }
    }
}

