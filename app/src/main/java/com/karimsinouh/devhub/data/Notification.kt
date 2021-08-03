package com.karimsinouh.devhub.data

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import java.util.*

data class Notification(
    val title:String?=null,
    val content:String?=null,
    val type:Int?=TYPE_FOLLOW,
    val action:String?=null,
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
        const val TYPE_UP_VOTE_REPLY=4
        const val TYPE_DOWN_VOTE_REPLY=5
    }



}
