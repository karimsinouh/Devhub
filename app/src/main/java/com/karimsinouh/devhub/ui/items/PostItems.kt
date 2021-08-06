package com.karimsinouh.devhub.ui.items

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.karimsinouh.devhub.data.Post
import com.karimsinouh.devhub.utils.SwipeAble
import com.karimsinouh.devhub.utils.customComposables.ChipsList


@Composable
fun PostItem(
    post: Post,
    onHashtagClick: (hashtag: String) -> Unit,
    onClick: () -> Unit
){
    when(post.type){

        0-> Question(post,onClick,onHashtagClick)

        1-> Announcement(post,onClick,onHashtagClick)

        2-> Tutorial(post,onClick)

    }
}

@Composable
fun SwipeAblePostItem(
    post: Post,
    onHashtagClick: (hashtag: String) -> Unit,
    onDelete:()->Unit,
    onEdit:()->Unit,
    onClick: () -> Unit
){



    SwipeAble(
        onEdit = onEdit,
        onDelete = onDelete
    ) {

        when(post.type){

            0-> Question(post,onClick,onHashtagClick)

            1-> Announcement(post,onClick,onHashtagClick)

            2-> Tutorial(post,onClick)

        }

    }
}


@Composable
fun Question(
    post: Post,
    onClick:()->Unit,
    onHashtagClick:(hashtag:String)->Unit
){
    Card(modifier=Modifier.padding(horizontal = 12.dp,0.dp)){
        Column(
            Modifier
                .clickable(onClick = onClick)
                .padding(12.dp)
        ) {
            Text(text = post.userName+" asked a question",fontSize = 12.sp)
            Text(
                text = post.title?:"",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            Row {
                post.hashtags?.let {tags->
                    ChipsList(list = tags){
                        onHashtagClick(it)
                    }
                }
            }
            Text(text = post.content?:"",maxLines = 5)
        }
    }
}


@Composable
fun Announcement(
    post: Post,
    onClick:()->Unit,
    onHashtagClick:(hashtag:String)->Unit
){

    val hasImages=post.images?.isNotEmpty()!!

    Card(modifier=Modifier.padding(horizontal = 12.dp,0.dp)){
        Column(
            Modifier
                .clickable(onClick = onClick)
                .padding(12.dp)
        ) {

            if(hasImages){
                val painter= rememberImagePainter(post.images!![0])
                Image(
                    painter = painter,
                    contentDescription = null,
                    modifier= Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colors.onSurface.copy(alpha = 0.2f)),
                    contentScale= ContentScale.Crop
                )
            }
            Text(text = post.userName+" announced",fontSize = 12.sp)
            Text(
                text = post.title?:"",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            Row {
                post.hashtags?.let {tags->
                    ChipsList(list = tags){
                        onHashtagClick(it)
                    }
                }
            }
            Text(text = post.content?:"",maxLines = 5)

        }
    }
}


@Composable
fun Tutorial(
    post: Post,
    onClick:()->Unit
){

    val hasImages=post.images?.isNotEmpty()!!

    Card(modifier=Modifier.padding(horizontal = 12.dp,0.dp)){
        Column(
            Modifier
                .clickable(onClick = onClick)
                .fillMaxWidth()
        ) {
            if(hasImages){
                val painter= rememberImagePainter(post.images!![0])
                Image(
                    painter = painter,
                    contentDescription = null,
                    modifier= Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(MaterialTheme.colors.onSurface.copy(alpha = 0.2f)),
                    contentScale= ContentScale.Crop
                )
            }
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = post.title?:"",
                    fontWeight = FontWeight.Bold,
                    fontSize = 19.sp
                )

                if (!hasImages){
                    Text(
                        text = "tutorial by ${post.userName?:"Some user"}",
                        fontSize = 12.sp,
                        fontStyle = FontStyle.Italic
                    )
                    Text(
                        text = post.content ?: "",
                        maxLines = 5
                    )
                }

            }
        }
    }
}
