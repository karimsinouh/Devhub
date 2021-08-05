package com.karimsinouh.devhub.ui.chat.chat

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.karimsinouh.devhub.data.ChatRoom
import com.karimsinouh.devhub.data.Message
import com.karimsinouh.devhub.data.Notification
import com.karimsinouh.devhub.data.User
import com.karimsinouh.devhub.ui.notifications.NotificationsRepository
import com.karimsinouh.devhub.utils.ScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val notificationsRepository: NotificationsRepository
) :ViewModel() {

    var chatRoomId:String?=null
    val currentUSer=Firebase.auth.currentUser!!

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

    val message= mutableStateOf("")

    fun sendTextMessage(){
        if (message.value!=""){
            Message.send(message.value,chatRoomId!!)

            if (!user.value?.online!!){
                val notification=Notification(
                    title="${currentUSer.displayName} sent you a message",
                    content = message.value,
                    type=Notification.TYPE_MESSAGE,
                    action = "${currentUSer.uid}}$chatRoomId",
                    receiverId = user.value?.id,
                    picture = currentUSer.photoUrl.toString()
                )
                notificationsRepository.sendNotification(notification.asData(user.value?.token))
            }

            message.value=""
        }
    }

    private fun loadMessages(){
        ChatRoom.getMessages(chatRoomId!!) {
            if (it.isSuccessful){
                val result=it.data ?: emptyList()
                messages.value=result

                if (result.isNotEmpty()){
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
        chatRoomId: String? = null
    ){

        this.chatRoomId=chatRoomId

        if(this.chatRoomId==null && chatRoomId==null)
            ChatRoom.get(currentUSer.uid,user.value?.id!!){
                if (it.isSuccessful){
                    this.chatRoomId=it.data
                    loadMessages()
                }else
                    error=it.message?:""

            }
        else if (this.chatRoomId!=null){
            loadMessages()
        }

    }

    fun loadUserFirst(uid:String,chatRoomId: String?=null){
        User.get(uid){
            if(it.isSuccessful){
                user.value=it.data
                Log.wtf("wtf","uid $uid")
                loadChatRoom(chatRoomId)
            }else{
                error=it.message
            }
        }
    }
}