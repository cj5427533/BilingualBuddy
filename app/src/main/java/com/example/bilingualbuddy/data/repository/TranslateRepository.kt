package com.example.bilingualbuddy.data.repository

import com.example.bilingualbuddy.util.Result

interface TranslateRepository {
    suspend fun translate(text: String, sourceLang: String, targetLang: String): Result<String>
}

