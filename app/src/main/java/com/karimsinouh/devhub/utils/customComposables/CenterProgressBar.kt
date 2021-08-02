package com.karimsinouh.devhub.utils.customComposables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun CenterProgress(height:Dp?=null){

    val modifier=if (height!=null)
        Modifier
        .height(height)
        .fillMaxWidth()
    else Modifier.fillMaxSize()

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ){
        CircularProgressIndicator(
            strokeWidth = 3.dp
        )
    }
}