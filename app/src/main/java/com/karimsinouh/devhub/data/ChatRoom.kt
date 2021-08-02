package com.karimsinouh.devhub.data

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

data class ChatRoom(
    val users:List<String>?= emptyList(),
    val lastMessage:Map<String,Any>?=null,
    val lastUpdate:Long?=System.currentTimeMillis(),
    @DocumentId val id:String?=null,
    @ServerTimestamp val createdOn:Date?=null,
){
    fun getLastMessage():Message{
        return lastMessage.let {
            Message(
                it!!["sender"] as String,
                it["message"] as String,
                it["type"] as Int,
                it["seen"] as Boolean,
            )
        }
    }


    companion object{
        fun getChatRoomsOf(id:String,listener:(Result<List<ChatRoom>>)->Unit){
            val ref= Firebase.firestore.collection("chatRooms").whereArrayContains("users",id)
            ref.addSnapshotListener { value, error ->
                if (error!=null){
                    listener(Result(false,null,error.message))
                    return@addSnapshotListener
                }

                if (value!=null){
                    val chatRooms=value.toObjects(ChatRoom::class.java)
                    val emptyMessage="It's a bit empty here, you haven't sent or received any messages yet"
                    listener(Result(chatRooms.isNotEmpty(),chatRooms,emptyMessage))
                }
            }
        }
    }


}