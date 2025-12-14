// ui/QuestionActivity.kt
package com.example.bilingualbuddy.ui.home

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.bilingualbuddy.R
import com.example.bilingualbuddy.api.AIService
import com.example.bilingualbuddy.api.MockAIService
import com.example.bilingualbuddy.api.OpenAiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class QuestionActivity : AppCompatActivity() {
    private lateinit var questionInput: EditText
    private lateinit var imagePreview: ImageView
    private lateinit var submitButton: Button
    private lateinit var uploadButton: Button
    private var selectedImageUri: Uri? = null
    private val aiService: AIService = MockAIService()

    private val imagePickerLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                selectedImageUri = uri
                imagePreview.setImageURI(uri)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question)

        questionInput = findViewById(R.id.questionInput)
        imagePreview = findViewById(R.id.imagePreview)
        submitButton = findViewById(R.id.submitButton)
        uploadButton = findViewById(R.id.uploadButton)

        uploadButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            imagePickerLauncher.launch(intent)
        }

        submitButton.setOnClickListener {
            val question = questionInput.text.toString()
            if (question.isNotEmpty()) {
                processQuestion(question)
            }
        }
    }

    private fun processQuestion(question: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val answer = aiService.getAnswer(question)
                withContext(Dispatchers.Main) {
                    val intent = Intent(this@QuestionActivity, ResultActivity::class.java).apply {
                        putExtra("vietnamese_summary", answer.vietnameseSummary)
                        putExtra("korean_explanation", answer.koreanExplanation)
                        putExtra("pronunciation", answer.pronunciation)
                    }
                    startActivity(intent)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    // 에러 처리
                }
            }
        }
    }
}