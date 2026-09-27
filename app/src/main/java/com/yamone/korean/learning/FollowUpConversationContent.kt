package com.yamone.korean.learning

/**
 * Short real-life conversation drills that end with a natural follow-up question.
 *
 * The existing conversation screen supplies offline Korean TTS/STT, local progress,
 * review handling, and the fixed learning-screen banner. Keeping this pack data-only
 * lets every existing conversation flow use it without adding a server dependency.
 */
object FollowUpConversationCatalog {
    val lessons: List<ConversationLesson> = listOf(
        ConversationLesson(
            id = "new_friend_followup",
            situation = followUpTr(
                "Meeting a new friend",
                "Conocer a un nuevo amigo",
                "Rencontrer un nouvel ami",
                "Gặp một người bạn mới",
                "พบเพื่อนใหม่",
                "Bertemu teman baru",
            ),
            partnerLine = "어디에서 왔어요?",
            partnerMeanings = followUpTr(
                "Where are you from?",
                "¿De dónde eres?",
                "Tu viens d'où ?",
                "Bạn đến từ đâu?",
                "มาจากที่ไหน?",
                "Anda berasal dari mana?",
            ),
            replies = listOf(
                ConversationReply(
                    id = "from_usa",
                    korean = "미국에서 왔어요.",
                    meanings = followUpTr(
                        "I'm from the United States.",
                        "Vengo de Estados Unidos.",
                        "Je viens des États-Unis.",
                        "Tôi đến từ Mỹ.",
                        "มาจากอเมริกา",
                        "Saya dari Amerika Serikat.",
                    ),
                ),
                ConversationReply(
                    id = "from_france",
                    korean = "프랑스에서 왔어요.",
                    meanings = followUpTr(
                        "I'm from France.",
                        "Vengo de Francia.",
                        "Je viens de France.",
                        "Tôi đến từ Pháp.",
                        "มาจากฝรั่งเศส",
                        "Saya dari Prancis.",
                    ),
                ),
                ConversationReply(
                    id = "from_vietnam",
                    korean = "베트남에서 왔어요.",
                    meanings = followUpTr(
                        "I'm from Vietnam.",
                        "Vengo de Vietnam.",
                        "Je viens du Vietnam.",
                        "Tôi đến từ Việt Nam.",
                        "มาจากเวียดนาม",
                        "Saya dari Vietnam.",
                    ),
                ),
            ),
            closingLine = "한국에 온 지 얼마나 됐어요?",
            closingMeanings = followUpTr(
                "How long have you been in Korea?",
                "¿Cuánto tiempo llevas en Corea?",
                "Depuis combien de temps es-tu en Corée ?",
                "Bạn đến Hàn Quốc được bao lâu rồi?",
                "มาเกาหลีได้นานแค่ไหนแล้ว?",
                "Sudah berapa lama Anda berada di Korea?",
            ),
        ),
        ConversationLesson(
            id = "restaurant_followup",
            situation = followUpTr(
                "Ordering food",
                "Pedir comida",
                "Commander au restaurant",
                "Gọi món",
                "สั่งอาหาร",
                "Memesan makanan",
            ),
            partnerLine = "매운 음식 괜찮으세요?",
            partnerMeanings = followUpTr(
                "Are you okay with spicy food?",
                "¿Le va bien la comida picante?",
                "La nourriture épicée vous convient ?",
                "Bạn ăn cay được không?",
                "ทานเผ็ดได้ไหมคะ/ครับ?",
                "Apakah Anda bisa makan makanan pedas?",
            ),
            replies = listOf(
                ConversationReply(
                    id = "spicy_ok",
                    korean = "네, 매운 음식 좋아해요.",
                    meanings = followUpTr(
                        "Yes, I like spicy food.",
                        "Sí, me gusta la comida picante.",
                        "Oui, j'aime la nourriture épicée.",
                        "Vâng, tôi thích đồ ăn cay.",
                        "ค่ะ/ครับ ชอบอาหารเผ็ด",
                        "Ya, saya suka makanan pedas.",
                    ),
                ),
                ConversationReply(
                    id = "not_spicy",
                    korean = "안 매운 걸로 주세요.",
                    meanings = followUpTr(
                        "Please give me something not spicy.",
                        "Algo que no sea picante, por favor.",
                        "Quelque chose de non épicé, s'il vous plaît.",
                        "Cho tôi món không cay.",
                        "ขอแบบไม่เผ็ดค่ะ/ครับ",
                        "Tolong yang tidak pedas.",
                    ),
                ),
                ConversationReply(
                    id = "little_spicy",
                    korean = "조금만 맵게 해 주세요.",
                    meanings = followUpTr(
                        "Please make it only a little spicy.",
                        "Que sea solo un poco picante, por favor.",
                        "Pas trop épicé, s'il vous plaît.",
                        "Làm hơi cay thôi nhé.",
                        "ขอเผ็ดนิดเดียวค่ะ/ครับ",
                        "Tolong sedikit pedas saja.",
                    ),
                ),
            ),
            closingLine = "음료는 뭐 드릴까요?",
            closingMeanings = followUpTr(
                "What would you like to drink?",
                "¿Qué le gustaría beber?",
                "Que souhaitez-vous boire ?",
                "Bạn muốn uống gì?",
                "จะรับเครื่องดื่มอะไรดีคะ/ครับ?",
                "Anda mau minum apa?",
            ),
        ),
        ConversationLesson(
            id = "weekend_followup",
            situation = followUpTr(
                "Talking about the weekend",
                "Hablar del fin de semana",
                "Parler du week-end",
                "Nói về cuối tuần",
                "คุยเรื่องวันหยุดสุดสัปดาห์",
                "Membicarakan akhir pekan",
            ),
            partnerLine = "주말에 뭐 했어요?",
            partnerMeanings = followUpTr(
                "What did you do over the weekend?",
                "¿Qué hizo el fin de semana?",
                "Qu'est-ce que vous avez fait ce week-end ?",
                "Cuối tuần bạn đã làm gì?",
                "สุดสัปดาห์ทำอะไรมาคะ/ครับ?",
                "Apa yang Anda lakukan akhir pekan lalu?",
            ),
            replies = listOf(
                ConversationReply(
                    id = "rested_home",
                    korean = "집에서 쉬었어요.",
                    meanings = followUpTr(
                        "I rested at home.",
                        "Descansé en casa.",
                        "Je me suis reposé à la maison.",
                        "Tôi nghỉ ở nhà.",
                        "พักอยู่บ้านค่ะ/ครับ",
                        "Saya beristirahat di rumah.",
                    ),
                ),
                ConversationReply(
                    id = "met_friend",
                    korean = "친구를 만났어요.",
                    meanings = followUpTr(
                        "I met a friend.",
                        "Me encontré con un amigo.",
                        "J'ai vu un ami.",
                        "Tôi gặp một người bạn.",
                        "ไปเจอเพื่อนมาค่ะ/ครับ",
                        "Saya bertemu teman.",
                    ),
                ),
                ConversationReply(
                    id = "watched_movie",
                    korean = "영화를 봤어요.",
                    meanings = followUpTr(
                        "I watched a movie.",
                        "Vi una película.",
                        "J'ai regardé un film.",
                        "Tôi đã xem phim.",
                        "ดูหนังมาค่ะ/ครับ",
                        "Saya menonton film.",
                    ),
                ),
            ),
            closingLine = "이번 주말에는 뭐 할 거예요?",
            closingMeanings = followUpTr(
                "What are you going to do this weekend?",
                "¿Qué va a hacer este fin de semana?",
                "Qu'est-ce que vous allez faire ce week-end ?",
                "Cuối tuần này bạn sẽ làm gì?",
                "สุดสัปดาห์นี้จะทำอะไรคะ/ครับ?",
                "Apa yang akan Anda lakukan akhir pekan ini?",
            ),
        ),
    )
}

private fun followUpTr(
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
