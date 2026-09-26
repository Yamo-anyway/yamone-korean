package com.yamone.korean.learning

data class BasicSentenceLesson(
    val id: String,
    val pattern: String,
    val korean: String,
    val correctChunks: List<String>,
    val choiceChunks: List<String>,
    val translations: Map<String, String>,
    val grammarNotes: Map<String, String>,
) {
    fun meaning(languageCode: String?): String =
        translations[languageCode] ?: translations.getValue("en")

    fun grammarNote(languageCode: String?): String =
        grammarNotes[languageCode] ?: grammarNotes.getValue("en")
}

object BasicSentenceCatalog {
    val lessons: List<BasicSentenceLesson> = listOf(
        BasicSentenceLesson(
            id = "sentence_identity",
            pattern = "저는 ___이에요/예요",
            korean = "저는 학생이에요.",
            correctChunks = listOf("저는", "학생이에요"),
            choiceChunks = listOf("학생이에요", "저는"),
            translations = translations(
                "I am a student.",
                "Soy estudiante.",
                "Je suis étudiant(e).",
                "Tôi là học sinh / sinh viên.",
                "ฉัน/ผมเป็นนักเรียน",
                "Saya seorang pelajar.",
            ),
            grammarNotes = translations(
                "저는 sets 'I' as the topic. 이에요/예요 politely means 'am/is/are'.",
                "저는 marca 'yo' como tema. 이에요/예요 es una forma cortés de 'ser'.",
                "저는 marque « je » comme thème. 이에요/예요 correspond poliment à « être ».",
                "저는 đặt 'tôi' làm chủ đề. 이에요/예요 là cách nói lịch sự của 'là'.",
                "저는 ทำให้ 'ฉัน/ผม' เป็นหัวข้อ และ 이에요/예요 ใช้บอกว่า 'เป็น' แบบสุภาพ",
                "저는 menandai 'saya' sebagai topik. 이에요/예요 berarti 'adalah' secara sopan.",
            ),
        ),
        BasicSentenceLesson(
            id = "sentence_like",
            pattern = "저는 ___을/를 좋아해요",
            korean = "저는 커피를 좋아해요.",
            correctChunks = listOf("저는", "커피를", "좋아해요"),
            choiceChunks = listOf("좋아해요", "저는", "커피를"),
            translations = translations(
                "I like coffee.",
                "Me gusta el café.",
                "J'aime le café.",
                "Tôi thích cà phê.",
                "ฉัน/ผมชอบกาแฟ",
                "Saya suka kopi.",
            ),
            grammarNotes = translations(
                "을/를 marks what you like. Korean normally places the action at the end.",
                "을/를 marca aquello que te gusta. En coreano, la acción suele ir al final.",
                "을/를 marque ce que l'on aime. En coréen, l'action se place généralement à la fin.",
                "을/를 đánh dấu đối tượng bạn thích. Trong tiếng Hàn, động từ thường đứng cuối câu.",
                "을/를 ใช้ชี้สิ่งที่ชอบ และภาษาเกาหลีมักวางคำกริยาไว้ท้ายประโยค",
                "을/를 menandai objek yang disukai. Dalam bahasa Korea, kata kerja biasanya berada di akhir.",
            ),
        ),
        BasicSentenceLesson(
            id = "sentence_go_school",
            pattern = "[시간] [장소]에 가요",
            korean = "오늘 학교에 가요.",
            correctChunks = listOf("오늘", "학교에", "가요"),
            choiceChunks = listOf("학교에", "가요", "오늘"),
            translations = translations(
                "I go to school today.",
                "Hoy voy a la escuela.",
                "Aujourd'hui, je vais à l'école.",
                "Hôm nay tôi đi học.",
                "วันนี้ฉัน/ผมไปโรงเรียน",
                "Hari ini saya pergi ke sekolah.",
            ),
            grammarNotes = translations(
                "에 marks the destination. A common order is time → place → action.",
                "에 marca el destino. Un orden común es tiempo → lugar → acción.",
                "에 marque la destination. Un ordre courant est temps → lieu → action.",
                "에 đánh dấu điểm đến. Thứ tự thường gặp là thời gian → nơi chốn → hành động.",
                "에 ใช้บอกจุดหมาย ลำดับที่พบบ่อยคือ เวลา → สถานที่ → การกระทำ",
                "에 menandai tujuan. Urutan umum adalah waktu → tempat → tindakan.",
            ),
        ),
        BasicSentenceLesson(
            id = "sentence_eat_now",
            pattern = "[시간] [대상]을/를 [동작]",
            korean = "지금 밥을 먹어요.",
            correctChunks = listOf("지금", "밥을", "먹어요"),
            choiceChunks = listOf("먹어요", "지금", "밥을"),
            translations = translations(
                "I am eating now.",
                "Estoy comiendo ahora.",
                "Je mange maintenant.",
                "Bây giờ tôi đang ăn.",
                "ตอนนี้ฉัน/ผมกำลังกินข้าว",
                "Saya sedang makan sekarang.",
            ),
            grammarNotes = translations(
                "The subject can be omitted when it is obvious. 밥을 is the object, and 먹어요 comes last.",
                "El sujeto puede omitirse si es obvio. 밥을 es el objeto y 먹어요 va al final.",
                "Le sujet peut être omis s'il est évident. 밥을 est l'objet et 먹어요 vient à la fin.",
                "Có thể lược chủ ngữ khi đã rõ. 밥을 là tân ngữ và 먹어요 đứng cuối.",
                "ละประธานได้เมื่อเข้าใจว่าเป็นใคร 밥을 คือกรรม และ 먹어요 อยู่ท้ายประโยค",
                "Subjek dapat dihilangkan jika sudah jelas. 밥을 adalah objek dan 먹어요 berada di akhir.",
            ),
        ),
        BasicSentenceLesson(
            id = "sentence_go_work",
            pattern = "[시간] [장소]에 가요",
            korean = "내일 회사에 가요.",
            correctChunks = listOf("내일", "회사에", "가요"),
            choiceChunks = listOf("가요", "내일", "회사에"),
            translations = translations(
                "I go to work tomorrow.",
                "Mañana voy al trabajo.",
                "Demain, je vais au travail.",
                "Ngày mai tôi đi làm.",
                "พรุ่งนี้ฉัน/ผมไปทำงาน",
                "Besok saya pergi bekerja.",
            ),
            grammarNotes = translations(
                "Change only the time or place to make many useful sentences with the same pattern.",
                "Cambia solo el tiempo o el lugar para crear muchas frases útiles con el mismo patrón.",
                "Change simplement le moment ou le lieu pour créer de nombreuses phrases utiles avec le même modèle.",
                "Chỉ cần đổi thời gian hoặc địa điểm là bạn có thể tạo nhiều câu hữu ích theo cùng mẫu.",
                "เปลี่ยนเพียงเวลาหรือสถานที่ ก็สร้างประโยคที่ใช้ได้จริงอีกมากด้วยรูปแบบเดิม",
                "Cukup ganti waktu atau tempat untuk membuat banyak kalimat berguna dengan pola yang sama.",
            ),
        ),
        BasicSentenceLesson(
            id = "sentence_friend_comes",
            pattern = "[주어]이/가 [장소]에 와요",
            korean = "친구가 집에 와요.",
            correctChunks = listOf("친구가", "집에", "와요"),
            choiceChunks = listOf("집에", "와요", "친구가"),
            translations = translations(
                "A friend comes to my home.",
                "Un amigo viene a mi casa.",
                "Un ami vient chez moi.",
                "Một người bạn đến nhà tôi.",
                "เพื่อนมาที่บ้าน",
                "Seorang teman datang ke rumah saya.",
            ),
            grammarNotes = translations(
                "이/가 marks the subject. 에 marks the destination, and 와요 ('comes') finishes the sentence.",
                "이/가 marca el sujeto. 에 marca el destino y 와요 ('viene') termina la oración.",
                "이/가 marque le sujet. 에 marque la destination et 와요 (« vient ») termine la phrase.",
                "이/가 đánh dấu chủ ngữ. 에 đánh dấu điểm đến và 와요 ('đến') đứng cuối câu.",
                "이/가 ใช้ชี้ประธาน 에 ใช้ชี้จุดหมาย และ 와요 ('มา') อยู่ท้ายประโยค",
                "이/가 menandai subjek. 에 menandai tujuan dan 와요 ('datang') berada di akhir kalimat.",
            ),
        ),
    )
}

private fun translations(
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
