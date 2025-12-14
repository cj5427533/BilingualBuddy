package com.example.bilingualbuddy.api

import com.example.bilingualbuddy.model.Answer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface AIService {
    suspend fun getAnswer(question: String): Answer
    suspend fun processImage(imageUri: String): Answer
}

class MockAIService : AIService {
    override suspend fun getAnswer(question: String): Answer = withContext(Dispatchers.IO) {
        // 실제 API 호출 대신 예시 응답 반환 (테스트용)
        // 질문에 따라 다른 답변 제공
        val answer = when {
            question.contains("2+4") || question.contains("더하기") -> {
                Answer(
                    vietnameseSummary = "2 cộng 4 bằng 6. Đây là phép tính cộng đơn giản.",
                    koreanExplanation = "2 더하기 4는 6입니다. 이것은 간단한 덧셈 문제입니다.",
                    pronunciation = "hai công bốn bằng sáu"
                )
            }
            question.contains("수도") || question.contains("서울") -> {
                Answer(
                    vietnameseSummary = "Thủ đô của Hàn Quốc là Seoul. Seoul là thành phố lớn nhất và là trung tâm văn hóa, kinh tế của Hàn Quốc.",
                    koreanExplanation = "한국의 수도는 서울입니다. 서울은 한국에서 가장 큰 도시이며 문화와 경제의 중심지입니다.",
                    pronunciation = "thủ đô của Hàn Quốc là Seoul"
                )
            }
            question.contains("화학식") || question.contains("물") -> {
                Answer(
                    vietnameseSummary = "Công thức hóa học của nước là H2O. Điều này có nghĩa là một phân tử nước bao gồm 2 nguyên tử hydro và 1 nguyên tử oxy.",
                    koreanExplanation = "물의 화학식은 H2O입니다. 이것은 물 분자가 수소 원자 2개와 산소 원자 1개로 이루어져 있다는 의미입니다.",
                    pronunciation = "công thức hóa học của nước là H hai O"
                )
            }
            question.contains("행성") || question.contains("태양계") -> {
                Answer(
                    vietnameseSummary = "Hệ mặt trời có 8 hành tinh: Sao Thủy, Sao Kim, Trái Đất, Sao Hỏa, Sao Mộc, Sao Thổ, Sao Thiên Vương và Sao Hải Vương.",
                    koreanExplanation = "태양계에는 8개의 행성이 있습니다: 수성, 금성, 지구, 화성, 목성, 토성, 천왕성, 해왕성입니다.",
                    pronunciation = "hệ mặt trời có tám hành tinh"
                )
            }
            question.contains("넓이") || question.contains("원") -> {
                Answer(
                    vietnameseSummary = "Diện tích của hình tròn được tính bằng công thức: π × r², trong đó r là bán kính của hình tròn và π (pi) xấp xỉ 3.14.",
                    koreanExplanation = "원의 넓이는 공식으로 계산합니다: π × r², 여기서 r은 원의 반지름이고 π(파이)는 약 3.14입니다.",
                    pronunciation = "diện tích của hình tròn bằng pi nhân r bình phương"
                )
            }
            question.contains("전통") || question.contains("음식") -> {
                Answer(
                    vietnameseSummary = "Các món ăn truyền thống của Hàn Quốc bao gồm kimchi, bulgogi, bibimbap, và nhiều món khác. Những món ăn này phản ánh văn hóa ẩm thực độc đáo của Hàn Quốc.",
                    koreanExplanation = "한국의 전통 음식에는 김치, 불고기, 비빔밥 등이 있습니다. 이러한 음식들은 한국의 독특한 식문화를 반영합니다.",
                    pronunciation = "các món ăn truyền thống của Hàn Quốc là kimchi, bulgogi, bibimbap"
                )
            }
            question.contains("자전") || question.contains("지구") -> {
                Answer(
                    vietnameseSummary = "Trái Đất quay quanh trục của nó một lần trong khoảng 24 giờ. Đây được gọi là chuyển động tự quay của Trái Đất, tạo ra ngày và đêm.",
                    koreanExplanation = "지구는 약 24시간에 한 번 자전축을 중심으로 회전합니다. 이것을 지구의 자전이라고 하며, 낮과 밤을 만들어냅니다.",
                    pronunciation = "Trái Đất quay quanh trục trong 24 giờ"
                )
            }
            else -> {
                Answer(
                    vietnameseSummary = "Đây là câu trả lời mẫu cho câu hỏi của bạn: $question. Vui lòng sử dụng OpenAI API để nhận câu trả lời chính xác hơn.",
                    koreanExplanation = "이것은 질문에 대한 예시 답변입니다: $question. 더 정확한 답변을 받으려면 OpenAI API를 사용해주세요.",
                    pronunciation = "đây là câu trả lời mẫu"
                )
            }
        }
        
        // 실제 API처럼 약간의 지연 시뮬레이션
        kotlinx.coroutines.delay(500)
        answer
    }

    override suspend fun processImage(imageUri: String): Answer = withContext(Dispatchers.IO) {
        Answer(
            vietnameseSummary = "숙제 사진을 분석하여 답변이 도출되었습니다.",
            koreanExplanation = "업로드한 숙제 사진을 AI가 분석하여 결과를 제공합니다.",
            pronunciation = "이것은 발음 예시입니다."
        )
    }
} 