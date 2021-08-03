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
    fun lastMessageAsObject():Message?{
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


    companion object{


        fun getMessages(id:String,listener: (Result<List<Message>>) -> Unit){
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

        fun get(myId:String,hisId:String,listener: (Result<String>) -> Unit){
            val ref=Firebase.firestore.collection("chatRooms").whereArrayContains("users",myId)

            ref.get().addOnCompleteListener { task->

                val chatRooms=task.result?.toObjects(ChatRoom::class.java) ?: emptyList()
                val commonChatroom=chatRooms.filter { it.users?.contains(hisId)!! }
                val chatRoomExists=commonChatroom.isNotEmpty()

                if (chatRoomExists){
                    listener(Result(true,commonChatroom[0].id))
                }else{
                    create(myId and hisId){
                        listener(Result(it.isSuccessful,it.data,it.message))
                    }
                }

            }
        }

        fun create(ids:List<String>,listener:(Result<String>)->Unit){
            val chatRoom=ChatRoom(ids)
            Firebase.firestore.collection("chatRooms").add(chatRoom)
                .addOnCompleteListener {
                    listener(Result(it.isSuccessful,it.result?.id,it.exception?.message))
                }
        }

        private infix fun String.and(secondId:String):List<String> {
            return listOf(this,secondId)
        }

    }


}