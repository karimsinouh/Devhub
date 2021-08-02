package com.karimsinouh.devhub.ui.chat.chat

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Send
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.karimsinouh.devhub.data.User
import com.karimsinouh.devhub.R
import com.karimsinouh.devhub.data.Message
import com.karimsinouh.devhub.ui.items.ProfilePicture
import com.karimsinouh.devhub.ui.theme.DevhubTheme
import com.karimsinouh.devhub.ui.theme.Shapes
import com.karimsinouh.devhub.utils.ScreenState
import com.karimsinouh.devhub.utils.customComposables.CenterProgress
import com.karimsinouh.devhub.utils.customComposables.MessageScreen
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ChatActivity:ComponentActivity() {

    companion object{
        fun open(
            c: Context,
            user: User,
            chatRoomId: String? = null
        ){
            val intent= Intent(c,ChatActivity::class.java)
            intent.putExtra("user",user)
            intent.putExtra("chatRoomId",chatRoomId)
            c.startActivity(intent)
        }
    }

    private val vm by viewModels<ChatViewModel>()
    private val uid= Firebase.auth.currentUser?.uid!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {


            DevhubTheme {
                window.statusBarColor=MaterialTheme.colors.surface.toArgb()
                Surface(color = MaterialTheme.colors.surface) {
                    Scaffold (
                        topBar = { ChatTopBar(vm.user.value!!) },
                        bottomBar = {MessageInput()},
                        content = {
                            Content()
                        }
                    )
                }
            }


        }

        vm.user.value=intent.getSerializableExtra("user") as User

        val chatRoomId=intent.getStringExtra("chatRoomId")

        vm.loadChatRoom(uid,chatRoomId)

    }


    @Composable
    private fun Content(){

        when(vm.state.value){

            ScreenState.IDLE->{
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    reverseLayout = true,
                    modifier=Modifier.padding(bottom = 78.dp)
                ) {

                    items(vm.messages.value){item->
                        RightMessage(message = item, isLastOne = false)
                    }

                }
            }

            ScreenState.ERROR->{
                MessageScreen(title = "Wtf", message = vm.error!!)
            }

            ScreenState.LOADING-> CenterProgress()

        }

    }

    @Composable
    private fun ChatTopBar(user:User) {
        TopAppBar(
            backgroundColor = MaterialTheme.colors.surface,
            contentColor = MaterialTheme.colors.onSurface,
            elevation = 0.dp
        ) {

            IconButton(onClick = {finish()}) {
                Icon(Icons.Outlined.ArrowBack,null)
            }

            ProfilePicture(
                url = user.picture!!,
                size = 40.dp,
                isOnline = false
            )


            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = user.name?:"Some User",
                    fontSize = 16.sp
                )
                Text(
                    text = "Online",
                    fontSize = 12.sp,
                    color=MaterialTheme.colors.onSurface.copy(alpha = 0.8f)
                )
            }
        }
    }

    @Composable
    @Preview
    private fun MessageInput() {
        TextField(
            value = "",
            onValueChange = {},
            shape = Shapes.medium,
            colors = TextFieldDefaults.textFieldColors(
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent
            ),
            placeholder = { Text(text = "Message..")},
            trailingIcon = {
                Row {

                    IconButton(onClick = {  }) {
                        Icon(painter = painterResource(R.drawable.ic_image), contentDescription = null)
                    }

                    IconButton(onClick = {  }) {
                        Icon(imageVector = Icons.Outlined.Send, contentDescription = null)
                    }

                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        )
    }

    @Composable
    private fun RightMessage(
        message:Message,
        isLastOne:Boolean
    ) {
        Column(
            modifier= Modifier
                .fillMaxWidth()
                .padding(start = 64.dp),
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = message.message?:"",
                color = MaterialTheme.colors.onPrimary,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colors.primary)
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            )


            if(isLastOne)
                Text(
                    text = "Seen",
                    fontStyle = FontStyle.Italic,
                    fontSize = 12.sp
                )

        }
    }

}