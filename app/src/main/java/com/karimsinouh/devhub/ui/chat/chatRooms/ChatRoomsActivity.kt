package com.karimsinouh.devhub.ui.chat.chatRooms

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.karimsinouh.devhub.ui.theme.DevhubTheme
import com.karimsinouh.devhub.utils.customComposables.MessageScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatRoomsActivity:ComponentActivity() {

    companion object{
        fun open(c:Context){
            c.startActivity(Intent(c, ChatRoomsActivity::class.java))
        }
    }

    private val vm by viewModels<ChatRoomsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            DevhubTheme {
                window.statusBarColor=MaterialTheme.colors.surface.toArgb()
                Surface(color = MaterialTheme.colors.surface) {
                    ChatPreview()
                }
            }


        }

    }

    @Composable
    @Preview(showBackground = true)
    fun ChatPreview() {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            TopAppBar(
                title = {Text("Messages")},
                navigationIcon = { IconButton(onClick = { finish()}) {
                    Icon(Icons.Outlined.ArrowBack,null)
                }},
                backgroundColor = MaterialTheme.colors.surface,
                contentColor = MaterialTheme.colors.onSurface,
                elevation = 0.dp,
            )

            TextField(
                value = vm.query.value,
                onValueChange = {vm.query.value=it},
                placeholder = { Text(text = "Search")},
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth(),
                trailingIcon = {
                    IconButton(onClick = {  }) {
                        Icon(
                            imageVector = Icons.Outlined.Search,
                            contentDescription = null,
                        )
                    }
                },
                colors=TextFieldDefaults.textFieldColors(
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent
                ),
                singleLine = true
            )

            MessageScreen(
                title = "No chat rooms yet",
                message = "It's a bit empty here, you haven't sent or received any messages yet",
                button = "Got it",
                onClick = { finish() }
            )

        }
    }

}