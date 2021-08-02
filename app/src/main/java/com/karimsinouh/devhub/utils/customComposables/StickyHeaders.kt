package com.karimsinouh.devhub.utils.customComposables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun StickyHeaderAction(
    text:String,
    buttonText:String,
    onClick:()->Unit
) {
    Row(
        modifier= Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.surface),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Comments",
            modifier=Modifier.weight(0.9f)
        )
        TextButton(onClick = { }) {
            Text(text = "View All")
        }
    }
}

@Composable
fun StickyHeaderToggle(
    expanded:Boolean,
    text:String,
    onToggle:()->Unit
) {
    Row(
        modifier= Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.surface),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            modifier=Modifier.weight(0.9f)
        )

        IconButton(onClick = onToggle) {
            val icon= if(expanded) Icons.Outlined.KeyboardArrowUp else Icons.Outlined.KeyboardArrowDown
            Icon(icon, null)
        }


    }

}