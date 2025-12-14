package com.example.bilingualbuddy.util

import android.graphics.Bitmap
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.korean.KoreanTextRecognizerOptions
import kotlinx.coroutines.tasks.await

object OcrUtil {
    private val recognizer = TextRecognition.getClient(KoreanTextRecognizerOptions.Builder().build())
    
    suspend fun extractTextFromBitmap(bitmap: Bitmap): Result<String> {
        return try {
            val image = InputImage.fromBitmap(bitmap, 0)
            val result = recognizer.process(image).await()
            val extractedText = result.text
            if (extractedText.isBlank()) {
                Result.Error("텍스트를 찾을 수 없습니다")
            } else {
                Result.Success(extractedText)
            }
        } catch (e: Exception) {
            Result.Error("OCR 처리 중 오류가 발생했습니다: ${e.message}")
        }
    }
    
    fun close() {
        recognizer.close()
    }
}

