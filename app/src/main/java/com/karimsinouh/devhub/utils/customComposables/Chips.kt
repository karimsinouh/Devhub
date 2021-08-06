package com.karimsinouh.devhub.utils.customComposables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun Chip(
    text:String,
    selected:Boolean=false,
    onClick:()->Unit
){
    val background=if (selected)
        MaterialTheme.colors.primary.copy(alpha = 0.1f)
    else
        MaterialTheme.colors.onBackground.copy(alpha = 0.1f)

    val contentColor=if (selected)
        MaterialTheme.colors.primary
    else
        MaterialTheme.colors.onBackground

    Box(
        modifier= Modifier
            .padding(0.dp, 8.dp, 8.dp, 8.dp)
            .clip(CircleShape)
            .background(background)
            .clickable(onClick=onClick)
    ) {
        Text(
            text = text,
            color =contentColor,
            modifier = Modifier
                .padding(12.dp, 8.dp)
        )
    }
}

@Composable
fun ChipsList(list:List<String>,onClick:(String)->Unit){
    if(list.isNotEmpty())
        Row(
            modifier= Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ){
            list.forEach {
                Chip(it){
                    onClick(it)
                }
            }
        }
}