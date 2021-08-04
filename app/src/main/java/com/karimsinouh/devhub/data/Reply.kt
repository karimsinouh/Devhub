package com.karimsinouh.devhub.data

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ServerTimestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

data class Reply(
    val userId:String?=null,
    val userName:String?=null,
    val reply:String?=null,
    val postId:String?=null,
    val upVotes:List<String>?= emptyList(),
    val downVotes:List<String> ?= emptyList(),
    val votes:Long?=0,
    @DocumentId val id:String?=null,
    @ServerTimestamp val date:Date?=null
){

    companion object{

        fun of(postId:String,listener:(Result<List<Reply>>)->Unit){
            Firebase.firestore.collection("posts/$postId/replies").orderBy("votes",Query.Direction.DESCENDING)
                .addSnapshotListener { value, error ->
                    if(error!=null){
                        listener(Result(false,null,error.message))
                        return@addSnapshotListener
                    }

                    if(value!=null)
                        listener(Result(true,value.toObjects(Reply::class.java)))
                }
        }

        fun on(postId: String,replyMessage:String,user:FirebaseUser){
            val name=user.displayName
            val uid=user.uid

            val reply=Reply(uid,name,replyMessage,postId)

            Firebase.firestore.collection("posts")
                .document(postId)
                .collection("replies")
                .add(reply)
        }

    }

    fun upVote(postId:String,uid:String){
        val ref=Firebase.firestore.document("posts/$postId/replies/$id")
        val alreadyUpVoted=upVotes?.contains(uid)!!
        val action=if (alreadyUpVoted) FieldValue.arrayRemove(uid) else FieldValue.arrayUnion(uid)

        ref.update("upVotes",action).addOnCompleteListener {
            if (it.isSuccessful){
                if (downVotes?.contains(uid)!!)
                    ref.update("downVotes",FieldValue.arrayRemove(uid))
                ref.update("votes",FieldValue.increment( if (alreadyUpVoted) -1 else 1 ))
            }
        }
    }

    fun downVote(postId:String,uid:String){
        val ref=Firebase.firestore.document("posts/$postId/replies/$id")
        val alreadyDownVoted=downVotes?.contains(uid)!!
        val action=if (alreadyDownVoted) FieldValue.arrayRemove(uid) else FieldValue.arrayUnion(uid)

        ref.update("downVotes",action).addOnCompleteListener {
            if (it.isSuccessful){
                if (upVotes?.contains(uid)!!)
                    ref.update("upVotes",FieldValue.arrayRemove(uid))
                ref.update("votes",FieldValue.increment( if (alreadyDownVoted) 1 else -1 ))
            }
        }
    }

}
