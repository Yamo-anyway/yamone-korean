package com.yamone.korean.ui.screens

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.yamone.korean.data.LearningProgressRepository

@Composable
fun ReviewDataProbe(){
    val context=LocalContext.current
    val repo=remember(context){LearningProgressRepository(context.applicationContext)}
}
