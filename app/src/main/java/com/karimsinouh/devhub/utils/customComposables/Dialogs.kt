package com.karimsinouh.devhub.utils.customComposables

import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ErrorDialog(error:String,onDismiss:()->Unit){
    AlertDialog(
        onDismissRequest =onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "Ok")
            }
        },
        modifier= Modifier.padding(32.dp),
        text = { Text(text = error) },
        title = { Text(text = "Error") }
    )
}