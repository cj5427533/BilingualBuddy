package com.example.bilingualbuddy.util

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import java.util.*

object TtsUtil {
    private var textToSpeech: TextToSpeech? = null
    private var isInitialized = false
    private var onInitListener: ((Boolean) -> Unit)? = null

    fun init(context: Context, onInit: (Boolean) -> Unit) {
        if (textToSpeech == null) {
            textToSpeech = TextToSpeech(context) { status ->
                isInitialized = status == TextToSpeech.SUCCESS
                if (isInitialized) {
                    textToSpeech?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                        override fun onStart(utteranceId: String?) {
                            Log.d("TtsUtil", "TTS started: $utteranceId")
                        }

                        override fun onDone(utteranceId: String?) {
                            Log.d("TtsUtil", "TTS completed: $utteranceId")
                        }

                        override fun onError(utteranceId: String?) {
                            Log.e("TtsUtil", "TTS error: $utteranceId")
                        }
                    })
                }
                onInitListener?.invoke(isInitialized)
                onInit(isInitialized)
            }
        }
    }

    fun speak(context: Context, text: String, language: String = "vi-VN") {
        if (!isInitialized) {
            init(context) { success ->
                if (success) {
                    speakText(text, language)
                }
            }
        } else {
            speakText(text, language)
        }
    }

    private fun speakText(text: String, language: String) {
        textToSpeech?.let { tts ->
            val locale = when (language) {
                "vi-VN" -> Locale("vi", "VN")
                "ko-KR" -> Locale("ko", "KR")
                else -> Locale.getDefault()
            }
            
            tts.language = locale
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "TTS_UTTERANCE_ID")
        }
    }

    fun shutdown() {
        textToSpeech?.stop()
        textToSpeech?.shutdown()
        textToSpeech = null
        isInitialized = false
    }
}