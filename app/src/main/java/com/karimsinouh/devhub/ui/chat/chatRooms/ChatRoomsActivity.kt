package com.karimsinouh.devhub.ui.chat.chatRooms

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.karimsinouh.devhub.R
import com.karimsinouh.devhub.ui.chat.chat.ChatActivity
import com.karimsinouh.devhub.ui.items.ChatRoomItem
import com.karimsinouh.devhub.ui.theme.DevhubTheme
import com.karimsinouh.devhub.utils.ScreenState
import com.karimsinouh.devhub.utils.customComposables.CenterProgress
import com.karimsinouh.devhub.utils.customComposables.MessageScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatRoomsActivity:ComponentActivity() {

    companion object{
        fun open(c:Context){
            c.startActivity(Intent(c, ChatRoomsActivity::class.java))
        }
    }

    private val uid=Firebase.auth.currentUser?.uid!!
    private val vm by viewModels<ChatRoomsViewModel>()

    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Devhub)
        setContent {

            DevhubTheme {
                window.statusBarColor=MaterialTheme.colors.surface.toArgb()

                BottomSheetScaffold(
                    topBar={ChatRoomsTopBar()},
                    sheetContent = {},
                    sheetPeekHeight = 0.dp,
                    backgroundColor = MaterialTheme.colors.surface
                ) {

                    Content()

                }

            }


        }

    }

    @Composable
    private fun Content(){
        when(vm.state.value){

            ScreenState.LOADING-> CenterProgress()

            ScreenState.IDLE->{
                Column {
                    ChatRoomsList()
                }
            }

            ScreenState.ERROR->{
                MessageScreen(
                    title ="Oops!",
                    message =vm.error?:"Something went wrong",
                    button = "Got it",
                    onClick = {finish()}
                )
            }
            ScreenState.DONE -> Unit
        }
    }

    @Composable
    private fun ChatRoomsTopBar(){
        TopAppBar(
            title = {Text("Messages")},
            navigationIcon = { IconButton(onClick = { finish()}) {
                Icon(Icons.Outlined.ArrowBack,null)
            }},
            backgroundColor = MaterialTheme.colors.surface,
            contentColor = MaterialTheme.colors.onSurface,
            elevation = 0.dp,
        )
    }

    @Composable
    private fun ChatRoomsList() {
        LazyColumn{
            items(
                items = vm.chatRooms.value,
                key = { it.id!! }
            ){item ->

                ChatRoomItem(chatRoom = item, uid = uid){user->
                    ChatActivity.open(this@ChatRoomsActivity,user,item.id)
                }
                Divider(modifier=Modifier.padding(start = 75.dp,end = 12.dp))
            }
        }
    }


}