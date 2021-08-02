package com.karimsinouh.devhub.utils.customComposables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun RoundedButton(
    text:String,
    modifier: Modifier=Modifier,
    onClick:()->Unit
){
    Button(
        onClick = onClick,
        shape = CircleShape,
        elevation = ButtonDefaults.elevation(0.dp,0.dp),
        modifier = modifier,
        contentPadding = PaddingValues(20.dp,12.dp)
    ) {
        Text(text =text)
    }
}


@Composable
fun OutlinedRoundedButton(
    text:String,
    modifier: Modifier=Modifier,
    onClick:()->Unit
){
    Button(
        onClick = onClick,
        shape = CircleShape,
        elevation = ButtonDefaults.elevation(0.dp,0.dp),
        modifier = modifier,
        contentPadding = PaddingValues(20.dp,12.dp),
        colors = ButtonDefaults.buttonColors(Color.Transparent,MaterialTheme.colors.primary),
        border = BorderStroke(2.dp,MaterialTheme.colors.primary)
    ) {
        Text(text =text)
    }
}