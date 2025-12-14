package com.example.bilingualbuddy.data.repository

import com.example.bilingualbuddy.model.Answer
import com.example.bilingualbuddy.util.Result

interface AIRepository {
    suspend fun getAnswer(question: String): Result<Answer>
    suspend fun processImage(imageUri: String): Result<Answer>
}

