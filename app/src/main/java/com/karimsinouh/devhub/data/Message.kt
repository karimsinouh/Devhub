package com.karimsinouh.devhub.data

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.HashMap

data class Message(
    val sender:String?=null,
    val message:String?=null,
    val type:Int?= TYPE_TEXT,
    var seen:Boolean?=false,
    @DocumentId val id:String?=null,
    @ServerTimestamp val timestamp:Date?=null
){

    companion object{
        const val TYPE_TEXT=0
        const val TYPE_IMAGE=1



        fun send(
            message: String,
            chatRoomId: String,
            type: Int?= TYPE_TEXT
        ){
            val chatRoomReference=Firebase.firestore.collection("chatRooms").document(chatRoomId)

            val uid=Firebase.auth.currentUser?.uid!!
            val messageObject=Message(uid, message, type)

            chatRoomReference.collection("messages").add(messageObject).addOnCompleteListener {
                if (it.isSuccessful){
                    chatRoomReference.update("lastMessage",messageObject.asMap())
                    chatRoomReference.update("lastUpdate",System.currentTimeMillis())
                }
            }

        }


    }


    fun asMap():Map<String,Any>{
        val map=HashMap<String,Any>()
        map["sender"]= sender!!
        map["message"]=message!!
        map["seen"]=seen!!
        map["type"]=type!!
        return map
    }

    fun makeAsSeen(chatRoomId:String){
        val roomReference=Firebase.firestore.collection("chatRooms").document(chatRoomId)
        roomReference.collection("messages").document(id!!).update("seen",true).addOnCompleteListener {
            roomReference.update("lastMessage",this.apply { seen=true })
        }
    }

}