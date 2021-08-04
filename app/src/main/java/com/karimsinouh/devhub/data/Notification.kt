package com.karimsinouh.devhub.data

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ServerTimestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

data class Notification(
    val title:String?=null,
    val content:String?=null,
    val type:Int?=TYPE_FOLLOW,
    val action:String?=null,
    var receiverId:String?=null,
    val picture:String?=null,
    val seen:Boolean?=false,
    @DocumentId val id:String?=null,
    @ServerTimestamp val date:Date?=null
){

    companion object{
        const val TYPE_FOLLOW=0
        const val TYPE_UP_VOTE=1
        const val TYPE_DOWN_VOTE=2
        const val TYPE_REPLY=3
        const val TYPE_MESSAGE=4

        fun fromMap(map:Map<String,String>):Notification{
            return Notification(
                map["title"],
                map["content"],
                map["type"]?.toInt(),
                map["action"],
                map["picture"],
                map["receiverId"]
            )
        }

        fun get(uid:String,listener:(Result<List<Notification>>)->Unit){
            val ref=Firebase.firestore
                .collection("users/$uid/notifications")
                .orderBy("date",Query.Direction.DESCENDING)

            ref.addSnapshotListener { value, error ->
                if(error!=null){
                    listener(Result(false,null,error.message))
                    return@addSnapshotListener
                }

                if (value!=null){
                    val notifications=value.toObjects(Notification::class.java)
                    listener(Result(true,notifications))
                }
            }
        }

    }

    fun asData(to:String?):NotificationData =
        NotificationData(to,this)

    fun makeAsSeen(){
        Firebase.firestore
            .document("users/$receiverId/notifications/$id")
            .update("seen",true)
    }

}


data class NotificationData(
    var to:String?=null,
    val data:Notification
){

    fun storeInDatabase(listener:(isSuccessful:Boolean)->Unit){
        Firebase.firestore
            .collection("users")
            .document(data.receiverId!!)
            .collection("notifications")
            .add(data).addOnCompleteListener {
                listener(it.isSuccessful)
            }
    }

}