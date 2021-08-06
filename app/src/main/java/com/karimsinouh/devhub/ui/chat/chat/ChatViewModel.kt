package com.karimsinouh.devhub.ui.chat.chat

import android.net.Uri
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
import com.karimsinouh.devhub.utils.Upload
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

    val pictureState= mutableStateOf(ScreenState.IDLE)

    val messages= mutableStateOf<List<Message>>(emptyList())

    val message= mutableStateOf("")

    fun sendTextMessage(){
        if (message.value!=""){
            Message.send(message.value,chatRoomId!!)
            pushNotification(Message.TYPE_TEXT)
            message.value=""
        }
    }

    private fun pushNotification(type:Int){
        if (!user.value?.online!!){
            val notification=Notification(
                title="${currentUSer.displayName} sent you a ${if (type==Message.TYPE_IMAGE) "picture" else "message"} ",
                content = if (type==Message.TYPE_IMAGE) "Click to open the picture" else message.value,
                type=Notification.TYPE_MESSAGE,
                action = "${currentUSer.uid}}$chatRoomId",
                receiverId = user.value?.id,
                picture = currentUSer.photoUrl.toString()
            )
            notificationsRepository.sendNotification(notification.asData(user.value?.token))
        }
    }

    fun sendPicture(uri: Uri){
        pictureState.value=ScreenState.LOADING
        Upload.image("chat/$chatRoomId",uri){
            if (it.isSuccessful){
                val url=it.data!!
                Message.send(url,chatRoomId!!,Message.TYPE_IMAGE)
                pushNotification(Message.TYPE_IMAGE)
                pictureState.value=ScreenState.IDLE
            }else{
                state.value=ScreenState.IDLE
                Log.d("ChatViewModel",it.message!!)
            }
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