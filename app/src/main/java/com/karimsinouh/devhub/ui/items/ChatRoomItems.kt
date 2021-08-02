package com.karimsinouh.devhub.ui.items

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.karimsinouh.devhub.R
import com.karimsinouh.devhub.data.ChatRoom
import com.karimsinouh.devhub.data.User
import com.karimsinouh.devhub.ui.theme.Shapes
import kotlin.random.Random
import kotlin.random.nextInt

@Composable
fun ChatRoomItem(
    chatRoom:ChatRoom,
    uid:String
){

    val user= rememberSaveable{
        mutableStateOf<User?>(null)
    }

    if (user.value==null){
        ChatRoomPlaceHolder()
        chatRoom.getOtherUser(uid){
            user.value=it
        }

    }else{
        Row(
            modifier = Modifier
                .padding(start = 12.dp, end = 12.dp, top = 6.dp, bottom = 6.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ){

            //profile picture
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_background),
                contentDescription = null,
                modifier = Modifier
                    .clip(CircleShape)
                    .size(55.dp)
            )

            //name and message
            Column(
                modifier=Modifier.weight(0.9f)
            ) {
                Text(
                    text = "Karim Sinouh",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "him: Hey there",
                    fontSize = 12.sp,
                    color=MaterialTheme.colors.onBackground.copy(alpha=0.8f)
                )
            }

            Box(modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(Color.Red))
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