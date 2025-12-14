package com.example.bilingualbuddy.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.bilingualbuddy.navigation.Screen
import com.example.bilingualbuddy.util.TtsUtil
import java.net.URLDecoder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultScreen(
    navController: NavController,
    vietnameseSummary: String?,
    koreanExplanation: String?,
    pronunciation: String?
) {
    val context = LocalContext.current
    
    val decodedVietnamese = remember(vietnameseSummary) {
        vietnameseSummary?.let { URLDecoder.decode(it, "UTF-8") } ?: ""
    }
    val decodedKorean = remember(koreanExplanation) {
        koreanExplanation?.let { URLDecoder.decode(it, "UTF-8") } ?: ""
    }
    val decodedPronunciation = remember(pronunciation) {
        pronunciation?.let { URLDecoder.decode(it, "UTF-8") } ?: ""
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("답변 보기", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(
                        onClick = { 
                            if (navController.previousBackStackEntry != null) {
                                navController.popBackStack()
                            } else {
                                navController.navigate(Screen.Home.route) {
                                    popUpTo(Screen.Home.route) { inclusive = true }
                                }
                            }
                        }
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "뒤로가기")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.08f),
                            Color.Transparent
                        )
                    )
                )
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                shape = RoundedCornerShape(18.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(32.dp)
                    )
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "AI가 답변을 준비했어요!",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = "필요하면 발음 버튼을 눌러 들을 수 있어요.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }

            AnswerCard(
                title = "베트남어 요약",
                body = decodedVietnamese,
                buttonText = "베트남어 발음 듣기",
                onSpeak = { TtsUtil.speak(context, decodedVietnamese, "vi-VN") },
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                onContainerColor = MaterialTheme.colorScheme.onSecondaryContainer
            )

            AnswerCard(
                title = "한국어 설명",
                body = decodedKorean,
                buttonText = "한국어 발음 듣기",
                onSpeak = { TtsUtil.speak(context, decodedKorean, "ko-KR") },
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                onContainerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                isKorean = true
            )

            AnswerCard(
                title = "발음 가이드",
                body = decodedPronunciation,
                buttonText = "발음 다시 듣기",
                onSpeak = { TtsUtil.speak(context, decodedPronunciation, "vi-VN") },
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                onContainerColor = MaterialTheme.colorScheme.onTertiaryContainer
            )
        }
    }
}

@Composable
private fun AnswerCard(
    title: String,
    body: String,
    buttonText: String,
    onSpeak: () -> Unit,
    containerColor: Color,
    onContainerColor: Color,
    isKorean: Boolean = false
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = containerColor
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = onContainerColor
            )
            
            // 한국어 설명인 경우 더 읽기 쉽게 표시
            if (isKorean) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val lines = body.split("\n").filter { it.isNotBlank() }
                        if (lines.size > 1) {
                            // 여러 줄인 경우 불릿 포인트로 표시
                            lines.forEach { line ->
                                Text(
                                    text = "• $line",
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        lineHeight = TextUnit(24f, TextUnitType.Sp)
                                    ),
                                    color = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.padding(vertical = 4.dp)
                                )
                            }
                        } else {
                            // 한 줄인 경우 그냥 표시
                            Text(
                                text = body,
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    lineHeight = TextUnit(24f, TextUnitType.Sp)
                                ),
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                        }
                    }
                }
            } else {
                Text(
                    text = body,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        lineHeight = TextUnit(20f, TextUnitType.Sp)
                    ),
                    color = onContainerColor.copy(alpha = 0.9f),
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
            
            Button(
                onClick = onSpeak,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(vertical = 14.dp)
            ) {
                Text(
                    text = buttonText,
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold)
                )
            }
        }
    }
}

