// api/TranslateApi.kt
package com.example.bilingualbuddy.api

import com.example.bilingualbuddy.BuildConfig
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

object TranslateApi {
    private val client = OkHttpClient()
    private val url = "https://openapi.naver.com/v1/papago/n2mt"
    
    fun translate(text: String, sourceLang: String, targetLang: String, callback: (String) -> Unit) {
        if (BuildConfig.PAPAGO_CLIENT_ID.isBlank() || BuildConfig.PAPAGO_CLIENT_SECRET.isBlank()) {
            callback("번역 API 키가 설정되지 않았습니다. local.properties에 PAPAGO_CLIENT_ID와 PAPAGO_CLIENT_SECRET을 추가해주세요.")
            return
        }
        
        val requestBody = FormBody.Builder()
            .add("source", sourceLang)
            .add("target", targetLang)
            .add("text", text)
            .build()
        
        val request = Request.Builder()
            .url(url)
            .addHeader("X-Naver-Client-Id", BuildConfig.PAPAGO_CLIENT_ID)
            .addHeader("X-Naver-Client-Secret", BuildConfig.PAPAGO_CLIENT_SECRET)
            .post(requestBody)
            .build()
        
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback("번역 요청 실패: ${e.message}")
            }
            
            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) {
                    callback("번역 API 오류: ${response.code}")
                    return
                }
                
                try {
                    val responseBody = response.body?.string() ?: ""
                    val json = JSONObject(responseBody)
                    val translatedText = json
                        .getJSONObject("message")
                        .getJSONObject("result")
                        .getString("translatedText")
                    callback(translatedText)
                } catch (e: Exception) {
                    callback("번역 결과 파싱 오류: ${e.message}")
                }
            }
        })
    }
}