package com.karimsinouh.devhub.utils.customComposables

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MessageScreen(
    title:String,
    message:String,
    button:String?=null,
    onClick:(()->Unit)?=null
) {
    Column(
        modifier=Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Text(
            text = title,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
        )

        Spacer(Modifier.height(4.dp))

        Text(
            text = message,
            color=MaterialTheme.colors.onBackground.copy(alpha=0.8f),
            modifier=Modifier.width(250.dp),
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(8.dp))

        button?.let { buttonText->
            RoundedButton(
                text = buttonText,
                onClick = onClick!!
            )
        }

    }
}