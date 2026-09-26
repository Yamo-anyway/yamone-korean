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
            situation = tr("At a cafe", "En una cafeteria", "Dans un cafe", "O quan ca phe", "ที่คาเฟ่", "Di kafe"),
            partnerLine = "어서 오세요. 뭐 드릴까요?",
            partnerMeanings = tr("Welcome. What would you like?", "Bienvenido. Que le gustaria?", "Bienvenue. Que desirez-vous ?", "Xin chao. Ban muon dung gi?", "ยินดีต้อนรับ รับอะไรดีคะ/ครับ?", "Selamat datang. Mau pesan apa?"),
            replies = listOf(
                ConversationReply("coffee", "커피 주세요.", tr("Coffee, please.", "Un cafe, por favor.", "Un cafe, s'il vous plait.", "Cho toi ca phe.", "ขอกาแฟค่ะ/ครับ", "Kopi, tolong.")),
                ConversationReply("water", "물 주세요.", tr("Water, please.", "Agua, por favor.", "De l'eau, s'il vous plait.", "Cho toi nuoc.", "ขอน้ำค่ะ/ครับ", "Air, tolong.")),
                ConversationReply("gimbap", "김밥 주세요.", tr("Gimbap, please.", "Gimbap, por favor.", "Un gimbap, s'il vous plait.", "Cho toi kimbap.", "ขอคิมบับค่ะ/ครับ", "Gimbap, tolong.")),
            ),
            closingLine = "네, 잠시만 기다려 주세요.",
            closingMeanings = tr("Sure. Please wait a moment.", "Claro. Espere un momento.", "Bien sur. Un instant.", "Vang. Vui long doi mot chut.", "ได้ค่ะ/ครับ กรุณารอสักครู่", "Baik. Mohon tunggu sebentar."),
        ),
        ConversationLesson(
            id = "today_plan",
            situation = tr("Today's plan", "El plan de hoy", "Le programme du jour", "Ke hoach hom nay", "แผนวันนี้", "Rencana hari ini"),
            partnerLine = "오늘 어디에 가요?",
            partnerMeanings = tr("Where are you going today?", "Adonde vas hoy?", "Ou vas-tu aujourd'hui ?", "Hom nay ban di dau?", "วันนี้ไปที่ไหน?", "Hari ini Anda pergi ke mana?"),
            replies = listOf(
                ConversationReply("school", "학교에 가요.", tr("I'm going to school.", "Voy a la escuela.", "Je vais a l'ecole.", "Toi di den truong.", "ไปโรงเรียน", "Saya pergi ke sekolah.")),
                ConversationReply("company", "회사에 가요.", tr("I'm going to work.", "Voy al trabajo.", "Je vais au travail.", "Toi di lam.", "ไปทำงาน", "Saya pergi bekerja.")),
                ConversationReply("cafe", "카페에 가요.", tr("I'm going to a cafe.", "Voy a una cafeteria.", "Je vais dans un cafe.", "Toi di quan ca phe.", "ไปคาเฟ่", "Saya pergi ke kafe.")),
                ConversationReply("home", "집에 가요.", tr("I'm going home.", "Voy a casa.", "Je rentre a la maison.", "Toi ve nha.", "กลับบ้าน", "Saya pulang.")),
            ),
            closingLine = "그래요? 잘 다녀오세요.",
            closingMeanings = tr("Really? Have a good trip.", "Ah, si? Que te vaya bien.", "Ah oui ? Bonne journee.", "Vay a? Di vui nhe.", "เหรอ? เดินทางดี ๆ นะ", "Oh begitu? Hati-hati di jalan."),
        ),
        ConversationLesson(
            id = "food_like",
            situation = tr("Talking with a friend", "Hablar con un amigo", "Parler avec un ami", "Noi chuyen voi ban", "คุยกับเพื่อน", "Berbicara dengan teman"),
            partnerLine = "한국 음식을 좋아해요?",
            partnerMeanings = tr("Do you like Korean food?", "Te gusta la comida coreana?", "Tu aimes la cuisine coreenne ?", "Ban co thich do an Han Quoc khong?", "ชอบอาหารเกาหลีไหม?", "Apakah Anda suka makanan Korea?"),
            replies = listOf(
                ConversationReply("korean_food", "네, 한국 음식을 좋아해요.", tr("Yes, I like Korean food.", "Si, me gusta la comida coreana.", "Oui, j'aime la cuisine coreenne.", "Co, toi thich do an Han Quoc.", "ใช่ ชอบอาหารเกาหลี", "Ya, saya suka makanan Korea.")),
                ConversationReply("bulgogi", "네, 불고기를 좋아해요.", tr("Yes, I like bulgogi.", "Si, me gusta el bulgogi.", "Oui, j'aime le bulgogi.", "Co, toi thich bulgogi.", "ใช่ ชอบบูลโกกิ", "Ya, saya suka bulgogi.")),
                ConversationReply("gimbap", "저는 김밥을 좋아해요.", tr("I like gimbap.", "Me gusta el gimbap.", "J'aime le gimbap.", "Toi thich kimbap.", "ฉัน/ผมชอบคิมบับ", "Saya suka gimbap.")),
            ),
            closingLine = "저도 좋아해요. 같이 먹어요.",
            closingMeanings = tr("I like it too. Let's eat together.", "A mi tambien. Comamos juntos.", "Moi aussi. Mangeons ensemble.", "Toi cung thich. Cung an nhe.", "ฉัน/ผมก็ชอบ ไปกินด้วยกันนะ", "Saya juga suka. Mari makan bersama."),
        ),
    )
}

private fun tr(en: String, es: String, fr: String, vi: String, th: String, id: String) = mapOf(
    "en" to en,
    "es" to es,
    "fr" to fr,
    "vi" to vi,
    "th" to th,
    "id" to id,
)
