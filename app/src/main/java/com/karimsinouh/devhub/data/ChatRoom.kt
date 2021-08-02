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
    fun getLastMessage():Message?{
        return lastMessage?.let {
            Message(
                it["sender"] as String,
                it["message"] as String,
                it["type"] as Int,
                it["seen"] as Boolean,
            )
        }
    }


    fun getOtherUser(myId:String,listener: (User) -> Unit){

        val targetUid=if (users?.get(0)?:"" ==myId)
            users!![1]
        else
            users!![0]

        User.get(targetUid,true){
            if (it.isSuccessful)
                listener(it.data!!)
        }

    }

    fun getMessages(listener: (Result<List<Message>>) -> Unit){
        Firebase.firestore.collection("chatRooms/$id/messages").addSnapshotListener { value, error ->
            if(error!=null){
                listener(Result(false,null,error.message))
                return@addSnapshotListener
            }

            if (value!=null){
                val messages=value.toObjects(Message::class.java)
                listener(Result(true,messages))
            }
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

        fun get(myId:String,hisId:String,chatRoomId:String?=null,listener: (Result<ChatRoom>) -> Unit){
            val ref=Firebase.firestore.collection("chatRooms")

            if (chatRoomId!=null)
                ref.document(chatRoomId).get()
                .addOnCompleteListener {
                    listener(Result(it.isSuccessful,it.result?.toObject(ChatRoom::class.java),it.exception?.message))
                }
            else
                ref.whereArrayContains("users",myId).get().addOnCompleteListener { task->
                    val chatRooms=task.result?.toObjects(ChatRoom::class.java) ?: emptyList()
                    val arrayWhereHeExists=chatRooms.filter { it.users?.contains(hisId)!! }
                    val chatRoom=if (arrayWhereHeExists.isEmpty()) null else arrayWhereHeExists[0]
                    listener(Result(arrayWhereHeExists.isNotEmpty(),chatRoom,"We couldn't find any chatroom"))
                }
        }

    }


}