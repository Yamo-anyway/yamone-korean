package com.yamone.korean.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yamone.korean.learning.*

@Composable
fun ReviewScreen(
    languageCode:String?,
    reviewLessonIds:Set<String>,
    onReviewResolved:(String)->Unit,
){
    val t=when(languageCode){
        "es"->listOf("Repaso","Vuelve a practicar lo que fallaste.","Nada pendiente.","Ya lo sé")
        "fr"->listOf("Révision","Repratique ce qui était difficile.","Rien à réviser.","Je le maîtrise")
        "vi"->listOf("Ôn tập","Luyện lại những phần bạn làm sai.","Không có mục cần ôn.","Tôi đã nhớ")
        "th"->listOf("ทบทวน","ฝึกสิ่งที่ทำพลาดอีกครั้ง","ไม่มีรายการที่ต้องทบทวน","จำได้แล้ว")
        "id"->listOf("Ulasan","Latih lagi bagian yang masih sulit.","Tidak ada yang perlu diulang.","Sudah paham")
        else->listOf("Review","Practice again what you missed.","Nothing needs review.","I know this now")
    }
    LazyColumn(
        Modifier.fillMaxSize().padding(20.dp),
        verticalArrangement=Arrangement.spacedBy(12.dp),
    ){
        item{
            Text(t[0],style=MaterialTheme.typography.headlineMedium)
            Text(t[1],modifier=Modifier.padding(top=6.dp,bottom=8.dp))
        }
        if(reviewLessonIds.isEmpty()) item{
            Card(Modifier.fillMaxWidth()){Text(t[2],modifier=Modifier.padding(18.dp))}
        } else items(reviewLessonIds.sorted()){id->
            val text=reviewKorean(id)
            Card(Modifier.fillMaxWidth()){
                Column(Modifier.padding(18.dp),verticalArrangement=Arrangement.spacedBy(10.dp)){
                    Text(text,style=MaterialTheme.typography.headlineSmall)
                    Button(Modifier.fillMaxWidth(),onClick={onReviewResolved(id)}){Text(t[3])}
                }
            }
        }
    }
}

private fun reviewKorean(id:String):String{
    BasicSyllableCatalog.byId(id)?.let{return it.syllable+"  ("+it.initial+" + "+it.vowel+")"}
    BasicWordCatalog.lessons.firstOrNull{it.id==id}?.let{return it.korean}
    BasicSentenceCatalog.lessons.firstOrNull{it.id==id}?.let{return it.korean}
    listOf("listening_","speaking_").firstOrNull{id.startsWith(it)}?.let{p->
        BasicSentenceCatalog.lessons.firstOrNull{it.id==id.removePrefix(p)}?.let{return it.korean}
    }
    if(id.startsWith("expression_")) SelfExpressionCatalog.lessons.firstOrNull{it.id==id.removePrefix("expression_")}?.let{
        return it.choices.firstOrNull()?.sentence?:it.template
    }
    if(id.startsWith("conversation_")) ConversationCatalog.lessons.firstOrNull{it.id==id.removePrefix("conversation_")}?.let{
        return it.partnerLine
    }
    return id
}
