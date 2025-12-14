package com.example.bilingualbuddy.di

import com.example.bilingualbuddy.api.AIService
import com.example.bilingualbuddy.api.MockAIService
import com.example.bilingualbuddy.api.OpenAiService
import com.example.bilingualbuddy.api.TranslateApi
import com.example.bilingualbuddy.data.repository.AIRepository
import com.example.bilingualbuddy.data.repository.AIRepositoryImpl
import com.example.bilingualbuddy.data.repository.TranslateRepository
import com.example.bilingualbuddy.data.repository.TranslateRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .build()
    }
    
    @Provides
    @Singleton
    fun provideAIService(): AIService {
        // 테스트용 Mock 서비스 사용
        // 실제 API 사용 시: return OpenAiService()
        return MockAIService()
    }
    
    @Provides
    @Singleton
    fun provideAIRepository(aiService: AIService): AIRepository {
        return AIRepositoryImpl(aiService)
    }
    
    @Provides
    @Singleton
    fun provideTranslateRepository(): TranslateRepository {
        return TranslateRepositoryImpl()
    }
}

