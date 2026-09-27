package com.yamone.korean.learning

enum class WordCategory {
    PEOPLE,
    FOOD,
    PLACE,
    TIME,
    ACTION,
    QUESTION,
    HEALTH,
}

data class BasicWordLesson(
    val id: String,
    val category: WordCategory,
    val korean: String,
    val syllables: String,
    val translations: Map<String, String>,
) {
    fun meaning(languageCode: String?): String =
        translations[languageCode] ?: translations.getValue("en")
}

object BasicWordCatalog {
    val lessons: List<BasicWordLesson> = listOf(
        BasicWordLesson("word_na", WordCategory.PEOPLE, "나", "나", translations("I / me", "yo", "je / moi", "tôi", "ฉัน / ผม", "saya")),
        BasicWordLesson("word_saram", WordCategory.PEOPLE, "사람", "사 · 람", translations("person", "persona", "personne", "người", "คน", "orang")),
        BasicWordLesson("word_chingu", WordCategory.PEOPLE, "친구", "친 · 구", translations("friend", "amigo / amiga", "ami / amie", "bạn", "เพื่อน", "teman")),
        BasicWordLesson("word_gajok", WordCategory.PEOPLE, "가족", "가 · 족", translations("family", "familia", "famille", "gia đình", "ครอบครัว", "keluarga")),
        BasicWordLesson("word_mul", WordCategory.FOOD, "물", "물", translations("water", "agua", "eau", "nước", "น้ำ", "air")),
        BasicWordLesson("word_bap", WordCategory.FOOD, "밥", "밥", translations("meal / rice", "comida / arroz", "repas / riz", "cơm / bữa ăn", "ข้าว / มื้ออาหาร", "nasi / makanan")),
        BasicWordLesson("word_keopi", WordCategory.FOOD, "커피", "커 · 피", translations("coffee", "café", "café", "cà phê", "กาแฟ", "kopi")),
        BasicWordLesson("word_eumsik", WordCategory.FOOD, "음식", "음 · 식", translations("food", "comida", "nourriture", "đồ ăn", "อาหาร", "makanan")),
        BasicWordLesson("word_jip", WordCategory.PLACE, "집", "집", translations("home / house", "casa", "maison", "nhà", "บ้าน", "rumah")),
        BasicWordLesson("word_hakgyo", WordCategory.PLACE, "학교", "학 · 교", translations("school", "escuela", "école", "trường học", "โรงเรียน", "sekolah")),
        BasicWordLesson("word_hoesa", WordCategory.PLACE, "회사", "회 · 사", translations("company / workplace", "empresa / trabajo", "entreprise / travail", "công ty / nơi làm việc", "บริษัท / ที่ทำงาน", "perusahaan / tempat kerja")),
        BasicWordLesson("word_hwajangsil", WordCategory.PLACE, "화장실", "화 · 장 · 실", translations("restroom", "baño", "toilettes", "nhà vệ sinh", "ห้องน้ำ", "toilet")),
        BasicWordLesson("word_oneul", WordCategory.TIME, "오늘", "오 · 늘", translations("today", "hoy", "aujourd'hui", "hôm nay", "วันนี้", "hari ini")),
        BasicWordLesson("word_naeil", WordCategory.TIME, "내일", "내 · 일", translations("tomorrow", "mañana", "demain", "ngày mai", "พรุ่งนี้", "besok")),
        BasicWordLesson("word_jigeum", WordCategory.TIME, "지금", "지 · 금", translations("now", "ahora", "maintenant", "bây giờ", "ตอนนี้", "sekarang")),
        BasicWordLesson("word_gayo", WordCategory.ACTION, "가요", "가 · 요", translations("go / am going", "ir / voy", "aller / je vais", "đi", "ไป", "pergi")),
        BasicWordLesson("word_wayo", WordCategory.ACTION, "와요", "와 · 요", translations("come / am coming", "venir / vengo", "venir / je viens", "đến", "มา", "datang")),
        BasicWordLesson("word_meogeoyo", WordCategory.ACTION, "먹어요", "먹 · 어 · 요", translations("eat / am eating", "comer / como", "manger / je mange", "ăn", "กิน", "makan")),
        BasicWordLesson("word_bwayo", WordCategory.ACTION, "봐요", "봐 · 요", translations("see / look", "ver / mirar", "voir / regarder", "xem / nhìn", "ดู / มอง", "lihat")),
        BasicWordLesson("word_joahaeyo", WordCategory.ACTION, "좋아해요", "좋 · 아 · 해 · 요", translations("like", "gustar", "aimer", "thích", "ชอบ", "suka")),
        BasicWordLesson("word_ireum", WordCategory.PEOPLE, "이름", "이 · 름", translations("name", "nombre", "nom", "tên", "ชื่อ", "nama")),
        BasicWordLesson("word_bibimbap", WordCategory.FOOD, "비빔밥", "비 · 빔 · 밥", translations("bibimbap", "bibimbap", "bibimbap", "bibimbap", "บิบิมบับ", "bibimbap")),
        BasicWordLesson("word_seoulyeok", WordCategory.PLACE, "서울역", "서 · 울 · 역", translations("Seoul Station", "estación de Seúl", "gare de Séoul", "ga Seoul", "สถานีโซล", "Stasiun Seoul")),
        BasicWordLesson("word_myeot_si", WordCategory.TIME, "몇 시", "몇 · 시", translations("what time", "qué hora", "quelle heure", "mấy giờ", "กี่โมง", "jam berapa")),
        BasicWordLesson("word_juseyo", WordCategory.ACTION, "주세요", "주 · 세 · 요", translations("please give me", "deme, por favor", "donnez-moi, s'il vous plaît", "cho tôi / vui lòng", "ขอ...หน่อย", "tolong beri saya")),
        BasicWordLesson("word_mwo", WordCategory.QUESTION, "뭐", "뭐", translations("what", "qué", "quoi", "gì", "อะไร", "apa")),
        BasicWordLesson("word_eolma", WordCategory.QUESTION, "얼마", "얼 · 마", translations("how much", "cuánto", "combien", "bao nhiêu", "เท่าไหร่", "berapa")),
        BasicWordLesson("word_eodi", WordCategory.QUESTION, "어디", "어 · 디", translations("where", "dónde", "où", "đâu", "ที่ไหน", "di mana")),
        BasicWordLesson("word_eotteoke", WordCategory.QUESTION, "어떻게", "어 · 떻 · 게", translations("how", "cómo", "comment", "như thế nào", "อย่างไร", "bagaimana")),
        BasicWordLesson("word_meori", WordCategory.HEALTH, "머리", "머 · 리", translations("head", "cabeza", "tête", "đầu", "หัว", "kepala")),
        BasicWordLesson("word_apayo", WordCategory.HEALTH, "아파요", "아 · 파 · 요", translations("hurts", "duele", "avoir mal", "đau", "เจ็บ", "sakit")),
        BasicWordLesson("word_dasi", WordCategory.ACTION, "다시", "다 · 시", translations("again", "otra vez", "encore", "lại", "อีกครั้ง", "lagi")),
        BasicWordLesson("word_malhaeyo", WordCategory.ACTION, "말해요", "말 · 해 · 요", translations("say / speak", "decir / hablar", "dire / parler", "nói", "พูด", "berbicara")),
        BasicWordLesson("word_mannayo", WordCategory.ACTION, "만나요", "만 · 나 · 요", translations("meet", "encontrarse", "se rencontrer", "gặp", "พบ", "bertemu")),
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
