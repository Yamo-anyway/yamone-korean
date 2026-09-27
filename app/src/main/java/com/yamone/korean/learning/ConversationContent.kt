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
        ConversationLesson(
            id = "greeting_intro",
            situation = tr(
                "Meeting someone for the first time",
                "Conocer a alguien por primera vez",
                "Rencontrer quelqu'un pour la première fois",
                "Gặp ai đó lần đầu",
                "เจอกันครั้งแรก",
                "Bertemu seseorang untuk pertama kalinya",
            ),
            partnerLine = "안녕하세요. 이름이 뭐예요?",
            partnerMeanings = tr(
                "Hello. What's your name?",
                "Hola. ¿Cómo te llamas?",
                "Bonjour. Comment tu t'appelles ?",
                "Xin chào. Bạn tên là gì?",
                "สวัสดี ชื่ออะไร?",
                "Halo. Siapa nama Anda?",
            ),
            replies = listOf(
                ConversationReply(
                    "name_maria",
                    "저는 마리아예요.",
                    tr("I'm Maria.", "Soy María.", "Je m'appelle Maria.", "Tôi là Maria.", "ฉันชื่อมาเรีย", "Saya Maria."),
                ),
                ConversationReply(
                    "name_john",
                    "저는 존이에요.",
                    tr("I'm John.", "Soy John.", "Je m'appelle John.", "Tôi là John.", "ผมชื่อจอห์น", "Saya John."),
                ),
                ConversationReply(
                    "nice_to_meet",
                    "만나서 반가워요.",
                    tr("Nice to meet you.", "Mucho gusto.", "Enchanté(e).", "Rất vui được gặp bạn.", "ยินดีที่ได้รู้จัก", "Senang bertemu dengan Anda."),
                ),
            ),
            closingLine = "저도 반가워요.",
            closingMeanings = tr(
                "Nice to meet you too.",
                "Igualmente, mucho gusto.",
                "Moi aussi, enchanté(e).",
                "Tôi cũng rất vui được gặp bạn.",
                "ยินดีที่ได้รู้จักเช่นกัน",
                "Saya juga senang bertemu dengan Anda.",
            ),
        ),
        ConversationLesson(
            id = "restaurant_order",
            situation = tr(
                "Ordering at a restaurant",
                "Pedir en un restaurante",
                "Commander au restaurant",
                "Gọi món ở nhà hàng",
                "สั่งอาหารที่ร้านอาหาร",
                "Memesan di restoran",
            ),
            partnerLine = "주문하시겠어요?",
            partnerMeanings = tr(
                "Would you like to order?",
                "¿Desea pedir?",
                "Vous souhaitez commander ?",
                "Bạn muốn gọi món không?",
                "จะสั่งอาหารไหมคะ/ครับ?",
                "Mau pesan sekarang?",
            ),
            replies = listOf(
                ConversationReply(
                    "bibimbap",
                    "비빔밥 하나 주세요.",
                    tr("One bibimbap, please.", "Un bibimbap, por favor.", "Un bibimbap, s'il vous plaît.", "Cho tôi một bibimbap.", "ขอบิบิมบับหนึ่งที่ค่ะ/ครับ", "Satu bibimbap, tolong."),
                ),
                ConversationReply(
                    "bulgogi",
                    "불고기 주세요.",
                    tr("Bulgogi, please.", "Bulgogi, por favor.", "Du bulgogi, s'il vous plaît.", "Cho tôi bulgogi.", "ขอบูลโกกิค่ะ/ครับ", "Bulgogi, tolong."),
                ),
                ConversationReply(
                    "water",
                    "물도 주세요.",
                    tr("Water too, please.", "Agua también, por favor.", "De l'eau aussi, s'il vous plaît.", "Cho tôi thêm nước.", "ขอน้ำด้วยค่ะ/ครับ", "Air juga, tolong."),
                ),
            ),
            closingLine = "네, 금방 준비해 드릴게요.",
            closingMeanings = tr(
                "Sure, we'll prepare it shortly.",
                "Claro, se lo preparamos enseguida.",
                "Bien sûr, nous allons le préparer tout de suite.",
                "Vâng, chúng tôi sẽ chuẩn bị ngay.",
                "ได้ค่ะ/ครับ เดี๋ยวจัดให้เลย",
                "Baik, segera kami siapkan.",
            ),
        ),
        ConversationLesson(
            id = "shopping_price",
            situation = tr(
                "Shopping",
                "De compras",
                "Faire des achats",
                "Mua sắm",
                "ซื้อของ",
                "Berbelanja",
            ),
            partnerLine = "어서 오세요. 찾으시는 게 있으세요?",
            partnerMeanings = tr(
                "Welcome. Are you looking for something?",
                "Bienvenido. ¿Busca algo?",
                "Bienvenue. Vous cherchez quelque chose ?",
                "Xin chào. Bạn đang tìm gì à?",
                "ยินดีต้อนรับ กำลังหาอะไรอยู่ไหมคะ/ครับ?",
                "Selamat datang. Ada yang sedang Anda cari?",
            ),
            replies = listOf(
                ConversationReply(
                    "how_much",
                    "이거 얼마예요?",
                    tr("How much is this?", "¿Cuánto cuesta esto?", "Combien coûte ceci ?", "Cái này bao nhiêu tiền?", "อันนี้เท่าไหร่?", "Berapa harga ini?"),
                ),
                ConversationReply(
                    "bigger",
                    "더 큰 사이즈 있어요?",
                    tr("Do you have a bigger size?", "¿Tiene una talla más grande?", "Vous avez une taille plus grande ?", "Có cỡ lớn hơn không?", "มีไซซ์ใหญ่กว่านี้ไหม?", "Ada ukuran yang lebih besar?"),
                ),
                ConversationReply(
                    "just_looking",
                    "그냥 보고 있어요.",
                    tr("I'm just looking.", "Solo estoy mirando.", "Je regarde seulement.", "Tôi chỉ xem thôi.", "แค่ดูอยู่ค่ะ/ครับ", "Saya hanya melihat-lihat."),
                ),
            ),
            closingLine = "네, 편하게 보세요.",
            closingMeanings = tr(
                "Sure, take your time.",
                "Claro, mire con calma.",
                "Bien sûr, prenez votre temps.",
                "Vâng, cứ xem thoải mái nhé.",
                "ได้ค่ะ/ครับ ดูตามสบายเลย",
                "Baik, silakan lihat-lihat.",
            ),
        ),
        ConversationLesson(
            id = "ask_directions",
            situation = tr(
                "Asking for directions",
                "Preguntar cómo llegar",
                "Demander son chemin",
                "Hỏi đường",
                "ถามทาง",
                "Menanyakan arah",
            ),
            partnerLine = "어디를 찾으세요?",
            partnerMeanings = tr(
                "What place are you looking for?",
                "¿Qué lugar busca?",
                "Quel endroit cherchez-vous ?",
                "Bạn đang tìm chỗ nào?",
                "กำลังหาที่ไหนอยู่?",
                "Tempat apa yang Anda cari?",
            ),
            replies = listOf(
                ConversationReply(
                    "subway",
                    "지하철역이 어디예요?",
                    tr("Where is the subway station?", "¿Dónde está la estación de metro?", "Où est la station de métro ?", "Ga tàu điện ngầm ở đâu?", "สถานีรถไฟใต้ดินอยู่ที่ไหน?", "Di mana stasiun kereta bawah tanah?"),
                ),
                ConversationReply(
                    "restroom",
                    "화장실이 어디예요?",
                    tr("Where is the restroom?", "¿Dónde está el baño?", "Où sont les toilettes ?", "Nhà vệ sinh ở đâu?", "ห้องน้ำอยู่ที่ไหน?", "Di mana toiletnya?"),
                ),
                ConversationReply(
                    "station",
                    "서울역에 어떻게 가요?",
                    tr("How do I get to Seoul Station?", "¿Cómo llego a la estación de Seúl?", "Comment aller à la gare de Séoul ?", "Đi ga Seoul như thế nào?", "ไปสถานีโซลอย่างไร?", "Bagaimana cara pergi ke Stasiun Seoul?"),
                ),
            ),
            closingLine = "저쪽으로 쭉 가세요.",
            closingMeanings = tr(
                "Go straight that way.",
                "Siga recto por allí.",
                "Allez tout droit par là.",
                "Đi thẳng theo hướng đó.",
                "ตรงไปทางนั้นเลย",
                "Jalan lurus ke arah sana.",
            ),
        ),
        ConversationLesson(
            id = "public_transport",
            situation = tr(
                "Using public transport",
                "Usar transporte público",
                "Prendre les transports en commun",
                "Đi phương tiện công cộng",
                "ใช้ขนส่งสาธารณะ",
                "Menggunakan transportasi umum",
            ),
            partnerLine = "어디까지 가세요?",
            partnerMeanings = tr(
                "Where are you going?",
                "¿Hasta dónde va?",
                "Vous allez où ?",
                "Bạn đi đến đâu?",
                "จะไปถึงไหน?",
                "Anda mau pergi ke mana?",
            ),
            replies = listOf(
                ConversationReply(
                    "seoul_station",
                    "서울역까지 가요.",
                    tr("I'm going to Seoul Station.", "Voy hasta la estación de Seúl.", "Je vais jusqu'à la gare de Séoul.", "Tôi đi đến ga Seoul.", "ไปถึงสถานีโซล", "Saya pergi ke Stasiun Seoul."),
                ),
                ConversationReply(
                    "airport",
                    "공항에 가요.",
                    tr("I'm going to the airport.", "Voy al aeropuerto.", "Je vais à l'aéroport.", "Tôi đi sân bay.", "ไปสนามบิน", "Saya pergi ke bandara."),
                ),
                ConversationReply(
                    "transfer",
                    "어디에서 갈아타요?",
                    tr("Where do I transfer?", "¿Dónde hago transbordo?", "Où dois-je changer ?", "Tôi chuyển tuyến ở đâu?", "ต้องเปลี่ยนสายที่ไหน?", "Di mana saya harus pindah?"),
                ),
            ),
            closingLine = "두 정거장 후에 내리세요.",
            closingMeanings = tr(
                "Get off after two stops.",
                "Baje después de dos paradas.",
                "Descendez après deux arrêts.",
                "Xuống sau hai trạm.",
                "ลงหลังจากสองป้าย",
                "Turun setelah dua pemberhentian.",
            ),
        )
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
