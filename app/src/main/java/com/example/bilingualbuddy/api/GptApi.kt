// api/GptApi.kt
package com.example.bilingualbuddy.api

object GptApi {
    fun getAIExplanation(question: String, callback: (List<String>) -> Unit) {
        // 실제 OpenAI API 연동 필요 (아래는 구조 예시)
        // 결과를 3줄로 분리해서 콜백
        callback(listOf("모국어 요약", "한국어 설명", "발음 예시"))
    }
}