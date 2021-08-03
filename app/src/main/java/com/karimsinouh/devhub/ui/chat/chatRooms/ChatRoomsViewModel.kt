package com.karimsinouh.devhub.ui.chat.chatRooms

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.karimsinouh.devhub.data.ChatRoom
import com.karimsinouh.devhub.utils.ScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatRoomsViewModel @Inject constructor():ViewModel() {

    init {
        loadChatRooms()
    }

    val chatRooms= mutableStateOf<List<ChatRoom>>(emptyList())

    val state= mutableStateOf(ScreenState.LOADING)

    var error:String ?= ""
    set(value) {
        state.value=if (value!="")
            ScreenState.ERROR
        else
            ScreenState.IDLE
        field=value
    }

    private fun loadChatRooms()=viewModelScope.launch{
        val uid=Firebase.auth.currentUser?.uid!!
        ChatRoom.getChatRoomsOf(uid){
            if(it.isSuccessful){
                chatRooms.value=it.data ?: emptyList()
                state.value=ScreenState.IDLE
            }else
                error=it.message
        }
    }

}