package com.karimsinouh.devhub.ui.items

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.karimsinouh.devhub.data.ChatRoom
import com.karimsinouh.devhub.data.Message
import com.karimsinouh.devhub.data.User
import com.karimsinouh.devhub.ui.theme.Red
import com.karimsinouh.devhub.ui.theme.Shapes
import kotlin.random.Random
import kotlin.random.nextInt


/**
 * uid: the current logged in user
 * **/
@Composable
fun ChatRoomItem(
    chatRoom:ChatRoom,
    uid:String,
    onClick:(user:User)->Unit
){

    val _user= rememberSaveable{
        mutableStateOf<User?>(null)
    }

    if (_user.value==null){
        ChatRoomPlaceHolder()
        chatRoom.getOtherUser(uid){
            _user.value=it
        }

    }else _user.value?.let{ user->
        Row(
            modifier = Modifier
                .clickable { onClick(user) }
                .padding(start = 12.dp, end = 12.dp, top = 10.dp, bottom = 10.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ){

            //profile picture

            ProfilePicture(size = 55.dp, url = user.picture!!, isOnline = user.online!!)

            //name and message
            Column(
                modifier=Modifier.weight(0.9f)
            ) {
                Text(
                    text = user.name ?: "Some User",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                chatRoom.lastMessageAsObject()?.let {

                    val sender=if (it.sender==uid) "You:" else "Him:"
                    val message = if(it.type==Message.TYPE_TEXT) it.message else "Picture"
                    Text(
                        text = "$sender $message",
                        fontSize = 12.sp,
                        color=MaterialTheme.colors.onBackground.copy(alpha=0.7f)
                    )

                }
            }

            chatRoom.lastMessageAsObject()?.let {
                if (!it.seen!! && it.sender!=uid)
                    Box(modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(Red))
            }
        }
    }
}

@Composable
private fun ChatRoomPlaceHolder(){
    Row(
        modifier = Modifier
            .padding(start = 12.dp, end = 12.dp, top = 6.dp, bottom = 6.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ){

        val color=MaterialTheme.colors.onBackground.copy(alpha = 0.3f)
        val widthRange= 60..250

        Box(
            modifier = Modifier
                .clip(CircleShape)
                .background(color)
                .size(55.dp)
        )

        Column {
            Box(modifier = Modifier
                .clip(Shapes.medium)
                .height(20.dp)
                .width(Random.nextInt(widthRange).dp)
                .background(color)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Box(modifier = Modifier
                .height(14.dp)
                .width(Random.nextInt(widthRange).dp)
                .clip(Shapes.medium)
                .background(color)
            )
        }

    }
}