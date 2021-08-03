package com.karimsinouh.devhub.ui.items

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.karimsinouh.devhub.R
import com.karimsinouh.devhub.data.User
import com.karimsinouh.devhub.ui.theme.Green

@Composable
fun UserItem(
    user: User,
    onClick:()->Unit
){

    Row(
        modifier= Modifier
            .fillMaxWidth()
            .clickable(onClick=onClick)
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ){

        ProfilePicture(size = 55.dp, url = user.picture!!, isOnline = user.online!!)

        Column {
            Text(
                text = user.name?:"Some User",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
            )

            user.skills?.let {
                if(it.isNotEmpty()){

                    val skills=StringBuilder()

                    it.forEach { skill->
                        skills.append("$skill, ")
                    }

                    Text(
                        text = skills.toString(),
                        fontSize = 12.sp,
                        color=MaterialTheme.colors.onBackground.copy(alpha = 0.7f),
                    )

                }
            }


        }
    }

}

@Composable
fun ProfilePicture(
    size: Dp,
    url:String,
    isOnline:Boolean,
){


    val painter= rememberImagePainter(url)


    Box(
        modifier=Modifier.size(size)
    ) {

        val offset=size-(size*0.25f)

        Image(
            painter = painter,
            contentDescription = null,
            modifier= Modifier
                .fillMaxSize()
                .clip(CircleShape),
            contentScale = ContentScale.Crop,
        )

        if (isOnline){
            Spacer(
                modifier = Modifier
                    .absoluteOffset(offset, offset)
                    .size((size * 0.18f))
                    .clip(CircleShape)
                    .background(Green)

            )
        }
    }
}