package com.karimsinouh.devhub.ui.viewPost

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material.icons.outlined.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.google.accompanist.pager.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.karimsinouh.devhub.R
import com.karimsinouh.devhub.data.Post
import com.karimsinouh.devhub.data.Reply
import com.karimsinouh.devhub.data.User
import com.karimsinouh.devhub.utils.Screen
import com.karimsinouh.devhub.utils.ScreenState
import com.karimsinouh.devhub.utils.customComposables.CenterProgress
import com.karimsinouh.devhub.utils.customComposables.ChipsList
import com.karimsinouh.devhub.utils.customComposables.StickyHeaderToggle
import java.util.*
import androidx.compose.animation.AnimatedVisibility as AnimatedVisibility

@ExperimentalPagerApi
@ExperimentalAnimationApi
@ExperimentalFoundationApi
@Composable
fun ViewPost(
    nav:NavController,
    postId: String,
    vm:ViewPostViewModel= viewModel()
) {

    SideEffect {
        vm.loadPost(postId)
    }

    when(vm.state.value){

        ScreenState.IDLE->{
            LazyColumn(
                modifier= Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colors.surface)
                    .padding(12.dp),
                //verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                if (vm.post.value?.images?.isNotEmpty()!!){
                    item {
                        PicturesPager(pictures = vm.post.value?.images!!)
                    }
                }

                //top section
                item {
                    TopSection(
                        vm=vm,
                        uid=vm.currentUser.uid,
                        nav=nav
                    )
                }

                item {
                    ChipsList(list = vm.post.value?.hashtags!!)
                }

                //content
                item {
                    Text(
                        text = vm.post.value?.content?:"",
                        color = MaterialTheme.colors.onBackground.copy(alpha = 0.8f),
                        lineHeight = 20.sp
                    )
                }

                stickyHeader {
                    StickyHeaderToggle(expanded = vm.showReplies.value, text = "Replies") {
                        vm.showReplies.value= !vm.showReplies.value
                    }
                }

                if (vm.showReplies.value){
                    item {
                        ReplyTextField(
                            value = vm.reply.value,
                            onValueChange = { vm.reply.value=it },
                            onSubmit = {
                                vm.reply()
                            }
                        )
                    }

                    items(vm.replies.value){item ->
                        ReplyItem(postId, item)
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }

            }
        }

        ScreenState.LOADING-> CenterProgress()

    }
}

@Composable
private fun TopSection(
    vm:ViewPostViewModel,
    uid:String,
    nav:NavController,
){

    val post=vm.post.value!!
    val user=vm.user.value

    Row {

        val voteType=when{
            post.downVotes?.contains(uid)!! ->VoteType.DOWN
            post.upVotes?.contains(uid)!! ->VoteType.UP
            else->VoteType.NONE
        }

        val votes=(post.upVotes?.size?:0).minus(post.downVotes.size)

        UpDownVote(
            votes = votes,
            onUpvote = {
                post.voteUp(uid)
                vm.onUpVote()
            },
            onDownVote = {
                post.voteDown(uid)
                vm.onDownVote()

            },
            voteType = voteType
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column {
            Text(
                text = post.title?:"",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.height(8.dp))

            if (user!=null)
                UserInfo(user,post.date){uid->
                    nav.navigate(Screen.ViewProfile.constructRoute(uid))
                }
        }

    }
}

@Composable
fun UserInfo(
    user: User,
    date: Date?,
    onUserClick:(uid:String)->Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        val painter= rememberImagePainter(user.picture!!)

        Image(
            painter =painter,
            contentDescription =null,
            modifier= Modifier
                .size(45.dp)
                .clip(CircleShape)
                .clickable { onUserClick(user.id!!) },
            contentScale = ContentScale.Crop
        )

        Column {
            Text(
                text = user.name?:"Some User",
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                modifier=Modifier.clickable { onUserClick(user.id!!) }
            )

            Text(
                text = date.toString(),
                fontSize=11.sp,
                maxLines = 1,
                color=MaterialTheme.colors.onBackground.copy(alpha = 0.7f)
            )
        }
    }
}


@Composable
fun UpDownVote(
    votes:Int,
    onUpvote:()->Unit,
    onDownVote:()->Unit,
    voteType:VoteType=VoteType.NONE

){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconButton(onClick = {
                onUpvote()
        }) {
            Icon(
                Icons.Outlined.KeyboardArrowUp,
                null,
                tint = if (voteType==VoteType.UP) MaterialTheme.colors.primary else MaterialTheme.colors.onBackground
            )
        }

        Text(text = votes.toString())

        IconButton(onClick = {
                onDownVote()
        }) {
            Icon(
                Icons.Outlined.KeyboardArrowDown,
                null,
                tint = if (voteType==VoteType.DOWN) MaterialTheme.colors.primary else MaterialTheme.colors.onBackground
            )
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun PicturesPager(
    pictures:List<String>
){

    val state= rememberPagerState(pageCount = pictures.size)

    Column {
        HorizontalPager(state = state) {page->
            val painter= rememberImagePainter(pictures[page])
           Image(
               painter = painter,
               contentDescription = null,
               modifier= Modifier
                   .height(200.dp)
                   .fillMaxWidth()
                   .clip(RoundedCornerShape(12.dp)),
               contentScale = ContentScale.Crop
           )
        }

        HorizontalPagerIndicator(
            pagerState = state,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp)
        )
    }
}

@Composable
fun ReplyTextField(
    value:String,
    onValueChange:(String)->Unit,
    onSubmit:()->Unit
){
    TextField(
        value = value,
        onValueChange = {onValueChange(it)},
        leadingIcon = {
            Icon(painter = painterResource(id = R.drawable.ic_comment), contentDescription = null)
        },
        trailingIcon = {
            IconButton(onClick = onSubmit) {
                Icon(imageVector = Icons.Outlined.Send, contentDescription = null)
            }
        },
        placeholder = { Text(text = "Comment") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, 8.dp)
    )
}

@Composable
fun ReplyItem(
    postId:String,
    reply: Reply
) {
    Card{
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
        ) {

            val uid=Firebase.auth.currentUser?.uid!!

            val voteType=when{
                reply.upVotes?.contains(uid)!! ->VoteType.UP
                reply.downVotes?.contains(uid)!! ->VoteType.DOWN
                else->VoteType.NONE
            }

            val votes=reply.upVotes.size.minus(reply.downVotes?.size?:0)
            UpDownVote(
                votes = votes,
                onUpvote = { reply.upVote(postId,uid) },
                onDownVote = { reply.downVote(postId,uid) },
                voteType=voteType
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(text =reply.date.toString(),fontSize = 12.sp)
                Text(
                    text = "${reply.userName} replied",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                Text(text = reply.reply?:"")
            }

        }
    }
}