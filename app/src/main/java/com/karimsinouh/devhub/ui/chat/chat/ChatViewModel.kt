package com.karimsinouh.devhub.ui.chat.chat

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.karimsinouh.devhub.data.ChatRoom
import com.karimsinouh.devhub.data.Message
import com.karimsinouh.devhub.data.User
import com.karimsinouh.devhub.utils.ScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor() :ViewModel() {

    var chatRoom:ChatRoom?=null
    val user= mutableStateOf<User?>(null)

    val state= mutableStateOf(ScreenState.LOADING)

    var error:String ?= ""
        set(value) {
            state.value=if (value!="")
                ScreenState.ERROR
            else
                ScreenState.IDLE
            field=value
    }

    val messages= mutableStateOf<List<Message>>(emptyList())

    private fun loadMessages(){
        chatRoom?.getMessages {
            if (it.isSuccessful){
                val result=it.data ?: emptyList()
                messages.value=result

                if (result.isEmpty()){
                    state.value=ScreenState.IDLE
                }else{
                    error="You haven't sent any messages yet"
                }

            }else{
                error=it.message
            }
        }
    }

    fun loadChatRoom(
        currentUSerId: String,
        chatRoomId: String? = null
    ){
        if (chatRoom==null){
            ChatRoom.get(currentUSerId,user.value?.id!!,chatRoomId){
                if (it.isSuccessful){
                    chatRoom=it.data!!
                    loadMessages()
                }else{
                    error=it.message?:""
                }
            }
        }
    }

}