package com.yamone.korean.learning

data class ConversationReply(
    val id: String,
    val korean: String,
    val meanings: Map<String, String>,
) {
    fun meaning(languageCode: String?): String = meanings[languageCode] ?: meanings.getValue("en")
}

data class ConversationLesson(
    val id: String,
    val situation: Map<String, String>,
    val partnerLine: String,
    val partnerMeanings: Map<String, String>,
    val replies: List<ConversationReply>,
    val closingLine: String,
    val closingMeanings: Map<String, String>,
) {
    fun situation(languageCode: String?): String = situation[languageCode] ?: situation.getValue("en")
    fun partnerMeaning(languageCode: String?): String = partnerMeanings[languageCode] ?: partnerMeanings.getValue("en")
    fun closingMeaning(languageCode: String?): String = closingMeanings[languageCode] ?: closingMeanings.getValue("en")
}

object ConversationCatalog {
    val lessons = listOf(
        ConversationLesson(
            id = "cafe_order",
            situation = tr(
                "At a café",
                "En una cafetería",
                "Dans un café",
                "Ở quán cà phê",
                "ที่คาเฟ่",
                "Di kafe",
            ),
            partnerLine = "어서 오세요. 뭐 드릴까요?",
            partnerMeanings = tr(
                "Welcome. What would you like?",
                "Bienvenido. ¿Qué le gustaría?",
                "Bienvenue. Qu'est-ce que vous désirez ?",
                "Xin chào. Bạn muốn dùng gì?",
                "ยินดีต้อนรับ รับอะไรดีคะ/ครับ?",
                "Selamat datang. Mau pesan apa?",
            ),
            replies = listOf(
                ConversationReply(
                    "coffee",
                    "커피 주세요.",
                    tr("Coffee, please.", "Un café, por favor.", "Un café, s'il vous plaît.", "Cho tôi cà phê.", "ขอกาแฟค่ะ/ครับ", "Kopi, tolong."),
                ),
                ConversationReply(
                    "water",
                    "물 주세요.",
                    tr("Water, please.", "Agua, por favor.", "De l'eau, s'il vous plaît.", "Cho tôi nước.", "ขอน้ำค่ะ/ครับ", "Air, tolong."),
                ),
                ConversationReply(
                    "gimbap",
                    "김밥 주세요.",
                    tr("Gimbap, please.", "Gimbap, por favor.", "Un gimbap, s'il vous plaît.", "Cho tôi kimbap.", "ขอคิมบับค่ะ/ครับ", "Gimbap, tolong."),
                ),
            ),
            closingLine = "네, 잠시만 기다려 주세요.",
            closingMeanings = tr(
                "Sure. Please wait a moment.",
                "Claro. Espere un momento, por favor.",
                "Bien sûr. Un instant, s'il vous plaît.",
                "Vâng. Vui lòng đợi một chút.",
                "ได้ค่ะ/ครับ กรุณารอสักครู่",
                "Baik. Mohon tunggu sebentar.",
            ),
        ),
        ConversationLesson(
            id = "today_plan",
            situation = tr(
                "Talking about today's plan",
                "Hablar del plan de hoy",
                "Parler du programme d'aujourd'hui",
                "Nói về kế hoạch hôm nay",
                "คุยเรื่องแผนวันนี้",
                "Membicarakan rencana hari ini",
            ),
            partnerLine = "오늘 어디에 가요?",
            partnerMeanings = tr(
                "Where are you going today?",
                "¿Adónde vas hoy?",
                "Où vas-tu aujourd'hui ?",
                "Hôm nay bạn đi đâu?",
                "วันนี้ไปที่ไหน?",
                "Hari ini Anda pergi ke mana?",
            ),
            replies = listOf(
                ConversationReply(
                    "school",
                    "학교에 가요.",
                    tr("I'm going to school.", "Voy a la escuela.", "Je vais à l'école.", "Tôi đi đến trường.", "ไปโรงเรียน", "Saya pergi ke sekolah."),
                ),
                ConversationReply(
                    "company",
                    "회사에 가요.",
                    tr("I'm going to work.", "Voy al trabajo.", "Je vais au travail.", "Tôi đi làm.", "ไปทำงาน", "Saya pergi bekerja."),
                ),
                ConversationReply(
                    "cafe",
                    "카페에 가요.",
                    tr("I'm going to a café.", "Voy a una cafetería.", "Je vais dans un café.", "Tôi đi quán cà phê.", "ไปคาเฟ่", "Saya pergi ke kafe."),
                ),
                ConversationReply(
                    "home",
                    "집에 가요.",
                    tr("I'm going home.", "Voy a casa.", "Je rentre à la maison.", "Tôi về nhà.", "กลับบ้าน", "Saya pulang."),
                ),
            ),
            closingLine = "그래요? 잘 다녀오세요.",
            closingMeanings = tr(
                "Really? Have a good trip.",
                "¿Ah, sí? Que te vaya bien.",
                "Ah oui ? Bonne journée.",
                "Vậy à? Đi vui nhé.",
                "เหรอ? เดินทางดี ๆ นะ",
                "Oh begitu? Hati-hati di jalan.",
            ),
        ),
        ConversationLesson(
            id = "food_like",
            situation = tr(
                "Talking with a friend",
                "Hablando con un amigo",
                "Parler avec un ami",
                "Nói chuyện với bạn",
                "คุยกับเพื่อน",
                "Berbicara dengan teman",
            ),
            partnerLine = "한국 음식을 좋아해요?",
            partnerMeanings = tr(
                "Do you like Korean food?",
                "¿Te gusta la comida coreana?",
                "Tu aimes la cuisine coréenne ?",
                "Bạn có thích đồ ăn Hàn Quốc không?",
                "ชอบอาหารเกาหลีไหม?",
                "Apakah Anda suka makanan Korea?",
            ),
            replies = listOf(
                ConversationReply(
                    "korean_food",
                    "네, 한국 음식을 좋아해요.",
                    tr("Yes, I like Korean food.", "Sí, me gusta la comida coreana.", "Oui, j'aime la cuisine coréenne.", "Có, tôi thích đồ ăn Hàn Quốc.", "ใช่ ชอบอาหารเกาหลี", "Ya, saya suka makanan Korea."),
                ),
                ConversationReply(
                    "bulgogi",
                    "네, 불고기를 좋아해요.",
                    tr("Yes, I like bulgogi.", "Sí, me gusta el bulgogi.", "Oui, j'aime le bulgogi.", "Có, tôi thích bulgogi.", "ใช่ ชอบบูลโกกิ", "Ya, saya suka bulgogi."),
                ),
                ConversationReply(
                    "gimbap",
                    "저는 김밥을 좋아해요.",
                    tr("I like gimbap.", "Me gusta el gimbap.", "J'aime le gimbap.", "Tôi thích kimbap.", "ฉัน/ผมชอบคิมบับ", "Saya suka gimbap."),
                ),
            ),
            closingLine = "저도 좋아해요. 같이 먹어요.",
            closingMeanings = tr(
                "I like it too. Let's eat together.",
                "A mí también me gusta. Comamos juntos.",
                "Moi aussi. Mangeons ensemble.",
                "Tôi cũng thích. Cùng ăn nhé.",
                "ฉัน/ผมก็ชอบ ไปกินด้วยกันนะ",
                "Saya juga suka. Mari makan bersama.",
            ),
        ),
    )
}

private fun tr(
    en: String,
    es: String,
    fr: String,
    vi: String,
    th: String,
    id: String,
): Map<String, String> = mapOf(
    "en" to en,
    "es" to es,
    "fr" to fr,
    "vi" to vi,
    "th" to th,
    "id" to id,
)
