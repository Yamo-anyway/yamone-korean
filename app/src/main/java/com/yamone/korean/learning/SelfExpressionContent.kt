package com.yamone.korean.learning

data class SelfExpressionChoice(
    val id: String,
    val slotText: String,
    val sentence: String,
    val meanings: Map<String, String>,
) {
    fun meaning(languageCode: String?): String =
        meanings[languageCode] ?: meanings.getValue("en")
}

data class SelfExpressionLesson(
    val id: String,
    val template: String,
    val prompts: Map<String, String>,
    val choices: List<SelfExpressionChoice>,
) {
    fun prompt(languageCode: String?): String =
        prompts[languageCode] ?: prompts.getValue("en")
}

object SelfExpressionCatalog {
    val lessons: List<SelfExpressionLesson> = listOf(
        SelfExpressionLesson(
            id = "expression_like",
            template = "저는 ___을/를 좋아해요.",
            prompts = expressionTranslations(
                "Choose something you really like.",
                "Elige algo que de verdad te guste.",
                "Choisis quelque chose que tu aimes vraiment.",
                "Chọn một thứ bạn thật sự thích.",
                "เลือกสิ่งที่คุณชอบจริง ๆ",
                "Pilih sesuatu yang benar-benar Anda sukai.",
            ),
            choices = listOf(
                SelfExpressionChoice(
                    id = "coffee",
                    slotText = "커피",
                    sentence = "저는 커피를 좋아해요.",
                    meanings = expressionTranslations("coffee", "café", "café", "cà phê", "กาแฟ", "kopi"),
                ),
                SelfExpressionChoice(
                    id = "korean_food",
                    slotText = "한국 음식",
                    sentence = "저는 한국 음식을 좋아해요.",
                    meanings = expressionTranslations("Korean food", "comida coreana", "cuisine coréenne", "đồ ăn Hàn Quốc", "อาหารเกาหลี", "makanan Korea"),
                ),
                SelfExpressionChoice(
                    id = "music",
                    slotText = "음악",
                    sentence = "저는 음악을 좋아해요.",
                    meanings = expressionTranslations("music", "música", "musique", "âm nhạc", "ดนตรี", "musik"),
                ),
                SelfExpressionChoice(
                    id = "movie",
                    slotText = "영화",
                    sentence = "저는 영화를 좋아해요.",
                    meanings = expressionTranslations("movies", "películas", "films", "phim", "ภาพยนตร์", "film"),
                ),
            ),
        ),
        SelfExpressionLesson(
            id = "expression_go",
            template = "오늘 ___에 가요.",
            prompts = expressionTranslations(
                "Choose where you are going today.",
                "Elige adónde vas hoy.",
                "Choisis où tu vas aujourd'hui.",
                "Chọn nơi bạn sẽ đi hôm nay.",
                "เลือกสถานที่ที่คุณจะไปวันนี้",
                "Pilih ke mana Anda pergi hari ini.",
            ),
            choices = listOf(
                SelfExpressionChoice(
                    id = "school",
                    slotText = "학교",
                    sentence = "오늘 학교에 가요.",
                    meanings = expressionTranslations("school", "escuela", "école", "trường học", "โรงเรียน", "sekolah"),
                ),
                SelfExpressionChoice(
                    id = "company",
                    slotText = "회사",
                    sentence = "오늘 회사에 가요.",
                    meanings = expressionTranslations("work / company", "trabajo / empresa", "travail / entreprise", "công ty / chỗ làm", "ที่ทำงาน / บริษัท", "kantor / perusahaan"),
                ),
                SelfExpressionChoice(
                    id = "home",
                    slotText = "집",
                    sentence = "오늘 집에 가요.",
                    meanings = expressionTranslations("home", "casa", "maison", "nhà", "บ้าน", "rumah"),
                ),
                SelfExpressionChoice(
                    id = "cafe",
                    slotText = "카페",
                    sentence = "오늘 카페에 가요.",
                    meanings = expressionTranslations("café", "cafetería", "café", "quán cà phê", "คาเฟ่", "kafe"),
                ),
            ),
        ),
        SelfExpressionLesson(
            id = "expression_eat",
            template = "지금 ___을/를 먹어요.",
            prompts = expressionTranslations(
                "Choose what you are eating now.",
                "Elige lo que estás comiendo ahora.",
                "Choisis ce que tu manges maintenant.",
                "Chọn món bạn đang ăn bây giờ.",
                "เลือกสิ่งที่คุณกำลังกินตอนนี้",
                "Pilih apa yang sedang Anda makan sekarang.",
            ),
            choices = listOf(
                SelfExpressionChoice(
                    id = "rice",
                    slotText = "밥",
                    sentence = "지금 밥을 먹어요.",
                    meanings = expressionTranslations("a meal / rice", "comida / arroz", "repas / riz", "cơm / bữa ăn", "ข้าว / มื้ออาหาร", "nasi / makanan"),
                ),
                SelfExpressionChoice(
                    id = "gimbap",
                    slotText = "김밥",
                    sentence = "지금 김밥을 먹어요.",
                    meanings = expressionTranslations("gimbap", "gimbap", "gimbap", "kimbap", "คิมบับ", "gimbap"),
                ),
                SelfExpressionChoice(
                    id = "bulgogi",
                    slotText = "불고기",
                    sentence = "지금 불고기를 먹어요.",
                    meanings = expressionTranslations("bulgogi", "bulgogi", "bulgogi", "bulgogi", "บูลโกกิ", "bulgogi"),
                ),
                SelfExpressionChoice(
                    id = "ramyeon",
                    slotText = "라면",
                    sentence = "지금 라면을 먹어요.",
                    meanings = expressionTranslations("ramyeon", "ramyeon", "ramyeon", "mì ramyeon", "รามยอน", "ramyeon"),
                ),
            ),
        ),
    )
}

private fun expressionTranslations(
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
