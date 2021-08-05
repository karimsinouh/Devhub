package com.karimsinouh.devhub.ui.items

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.karimsinouh.devhub.data.Notification
import com.karimsinouh.devhub.ui.theme.Red
import com.karimsinouh.devhub.ui.theme.Shapes


@Composable
fun NotificationItem(
    notification:Notification,
    onClick:()->Unit
) {
    Card {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colors.surface)
                .clickable(onClick=onClick)
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            val icon=when(notification.type!!){
                Notification.TYPE_DOWN_VOTE->Icons.Default.KeyboardArrowDown
                Notification.TYPE_UP_VOTE->Icons.Default.KeyboardArrowUp
                else->Icons.Default.Notifications
            }

            Box(
                modifier = Modifier
                    .size(55.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colors.primary),
                contentAlignment = Alignment.Center
            ) {

                if(notification.type==Notification.TYPE_MESSAGE || notification.type==Notification.TYPE_FOLLOW)
                    ProfilePicture(
                        size = 55.dp,
                        url = notification.picture!!,
                        isOnline = false
                    )
                else
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = MaterialTheme.colors.onPrimary
                    )

            }


            Column(modifier = Modifier.weight(0.9f)) {
                Text(
                    text = notification.title?:"",
                    fontWeight = Bold,
                    fontSize = 18.sp,
                    maxLines = 2
                )

                Text(
                    text = notification.content!!,
                    fontStyle = FontStyle.Italic,
                    fontSize = 12.sp,
                    maxLines = 1,
                )
            }

            if (!notification.seen!!)
                Text(
                    text = "NEW",
                    modifier = Modifier
                        .clip(Shapes.small)
                        .background(Red)
                        .padding(4.dp),
                    color = Color.White,
                    fontSize=10.sp
                )

        }
    }
}