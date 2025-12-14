// ui/ResultActivity.kt
package com.example.bilingualbuddy.ui.home

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.bilingualbuddy.R
import com.example.bilingualbuddy.api.GptApi
import com.example.bilingualbuddy.api.TranslateApi
import com.example.bilingualbuddy.util.TtsUtil

class ResultActivity : AppCompatActivity() {
    private lateinit var vietnameseSummary: TextView
    private lateinit var koreanExplanation: TextView
    private lateinit var pronunciation: TextView
    private lateinit var btnTtsVietnamese: Button
    private lateinit var btnTtsKorean: Button
    private lateinit var backButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        initializeViews()
        setupListeners()
        displayResults()
    }

    private fun initializeViews() {
        vietnameseSummary = findViewById(R.id.vietnameseSummary)
        koreanExplanation = findViewById(R.id.koreanExplanation)
        pronunciation = findViewById(R.id.pronunciation)
        btnTtsVietnamese = findViewById(R.id.btnTtsVietnamese)
        btnTtsKorean = findViewById(R.id.btnTtsKorean)
        backButton = findViewById(R.id.backButton)
    }

    private fun setupListeners() {
        btnTtsVietnamese.setOnClickListener {
            vietnameseSummary.text.toString().let { text ->
                if (text.isNotEmpty()) {
                    TtsUtil.speak(this, text, "vi-VN")
                }
            }
        }

        btnTtsKorean.setOnClickListener {
            koreanExplanation.text.toString().let { text ->
                if (text.isNotEmpty()) {
                    TtsUtil.speak(this, text, "ko-KR")
                }
            }
        }

        backButton.setOnClickListener {
            finish()
        }
    }

    private fun displayResults() {
        intent.extras?.let { extras ->
            vietnameseSummary.text = extras.getString("vietnamese_summary")
            koreanExplanation.text = extras.getString("korean_explanation")
            pronunciation.text = extras.getString("pronunciation")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        TtsUtil.shutdown()
    }
}