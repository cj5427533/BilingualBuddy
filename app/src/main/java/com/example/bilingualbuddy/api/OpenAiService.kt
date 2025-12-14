package com.example.bilingualbuddy.api

import com.example.bilingualbuddy.model.Answer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import android.util.Log

class OpenAiService : AIService {
    private val apiKey = com.example.bilingualbuddy.BuildConfig.OPENAI_API_KEY
    private val client = OkHttpClient()
    private val url = "https://api.openai.com/v1/chat/completions"

    override suspend fun getAnswer(question: String): Answer = withContext(Dispatchers.IO) {
        if (apiKey.isBlank()) {
            throw Exception("OpenAI API 키가 설정되지 않았습니다. local.properties 파일에 OPENAI_API_KEY를 추가해주세요.")
        }
        
        val prompt = "아래 문장을 베트남어로 요약해주고, 한국어로 해설해주고, 베트남어 발음도 알려줘. 결과는 각각 [베트남어 요약], [한국어 설명], [발음]으로 구분해서 반환해줘. 문장: $question"
        val json = JSONObject()
        // gpt-4o-mini 또는 gpt-4-turbo로 변경 (사용 가능한 모델로)
        json.put("model", "gpt-4o-mini")
        val messages = JSONArray()
        messages.put(JSONObject().apply {
            put("role", "user")
            put("content", prompt)
        })
        json.put("messages", messages)

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        val request = Request.Builder()
            .url(url)
            .addHeader("Authorization", "Bearer $apiKey")
            .addHeader("Content-Type", "application/json")
            .post(body)
            .build()

        val response = client.newCall(request).execute()
        
        if (!response.isSuccessful) {
            val errorBody = response.body?.string() ?: "Unknown error"
            Log.e("OpenAiService", "API Error: ${response.code} - $errorBody")
            
            // 에러 메시지 파싱하여 사용자 친화적인 메시지 제공
            val errorMessage = try {
                val errorJson = JSONObject(errorBody)
                val error = errorJson.optJSONObject("error")
                if (error != null) {
                    val message = error.optString("message", "")
                    val code = error.optString("code", "")
                    
                    when {
                        code == "model_not_found" || message.contains("does not have access to model") -> {
                            "사용 가능한 모델이 없습니다. OpenAI 계정 설정을 확인해주세요."
                        }
                        response.code == 401 -> {
                            "API 키가 유효하지 않습니다. local.properties 파일의 OPENAI_API_KEY를 확인해주세요."
                        }
                        response.code == 403 -> {
                            "API 접근 권한이 없습니다. OpenAI 계정의 모델 접근 권한을 확인해주세요."
                        }
                        response.code == 429 -> {
                            "API 사용량 한도를 초과했습니다. 잠시 후 다시 시도해주세요."
                        }
                        else -> {
                            "OpenAI API 오류 (${response.code}): $message"
                        }
                    }
                } else {
                    "OpenAI API 오류: ${response.code}"
                }
            } catch (e: Exception) {
                "OpenAI API 오류: ${response.code} - $errorBody"
            }
            
            throw Exception(errorMessage)
        }
        
        val responseBody = response.body?.string() ?: throw Exception("No response from OpenAI")
        Log.d("OpenAiService", "responseBody: $responseBody")
        
        val responseJson = JSONObject(responseBody)
        
        if (!responseJson.has("choices")) {
            throw Exception("Invalid response format from OpenAI")
        }
        
        val choices = responseJson.getJSONArray("choices")
        if (choices.length() == 0) {
            throw Exception("No choices in response")
        }
        
        val answerText = choices
            .getJSONObject(0)
            .getJSONObject("message")
            .getString("content")

        // [베트남어 요약], [한국어 설명], [발음]으로 구분해서 파싱
        val vietnameseSummary = Regex("\\[베트남어 요약\\](.*?)(\\[|$)", RegexOption.DOT_MATCHES_ALL)
            .find(answerText)?.groups?.get(1)?.value?.trim() ?: ""
        val koreanExplanation = Regex("\\[한국어 설명\\](.*?)(\\[|$)", RegexOption.DOT_MATCHES_ALL)
            .find(answerText)?.groups?.get(1)?.value?.trim() ?: ""
        val pronunciation = Regex("\\[발음\\](.*?)(\\[|$)", RegexOption.DOT_MATCHES_ALL)
            .find(answerText)?.groups?.get(1)?.value?.trim() ?: ""

        Answer(
            vietnameseSummary = vietnameseSummary,
            koreanExplanation = koreanExplanation,
            pronunciation = pronunciation
        )
    }

    override suspend fun processImage(imageUri: String): Answer = withContext(Dispatchers.IO) {
        // 이미지 URI를 처리하여 OCR로 텍스트 추출 후 질문으로 변환
        // 실제 구현은 ViewModel에서 OCR 처리 후 getAnswer 호출
        throw UnsupportedOperationException("이미지 처리는 OCR을 통해 텍스트 추출 후 질문으로 변환해야 합니다")
    }
} 