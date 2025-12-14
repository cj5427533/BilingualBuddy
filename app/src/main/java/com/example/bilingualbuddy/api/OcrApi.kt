// api/OcrApi.kt
package com.example.bilingualbuddy.api

import android.graphics.Bitmap

object OcrApi {
    fun extractTextFromBitmap(bitmap: Bitmap, callback: (String) -> Unit) {
        // ML Kit OCR 연동 예시 (실제 구현 필요)
        // ...
        // callback("추출된 텍스트")
    }
}