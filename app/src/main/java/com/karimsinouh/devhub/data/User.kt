package com.karimsinouh.devhub.data

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.io.Serializable
import java.util.*

data class User(
    val id:String?=null,
    val name:String?=null,
    val email:String?=null,
    val bio:String?=null,
    val skills:List<String>?= emptyList(),
    val picture:String?="https://startupmission.kerala.gov.in/get-image-view/ksum_team/1624432404.jpeg",
    val followers:List<String>?= emptyList(),
    val following:List<String>?= emptyList(),
    var searchText:String?=null,
    var searchKeywords:List<String>?= emptyList(),
    val isOnline:Boolean?=false,
    val github:String?="",
    val behance:String?="",
    val dribble:String?="",
    @ServerTimestamp val joinedIn: Date?=null
):Serializable{

    companion object{
        fun get(id:String?=Firebase.auth.currentUser?.uid,realtime:Boolean=false,listener:(Result<User>)->Unit){

            val ref=Firebase.firestore.collection("users").document(id!!)

            if (realtime)
                ref.addSnapshotListener { value, error ->
                    if(error!=null){
                        listener(Result(false,null,error.message))
                        return@addSnapshotListener
                    }

                    if (value!=null)
                        listener(Result(true,value.toObject(User::class.java)))
                }
            else
                ref.get().addOnCompleteListener {
                    listener(Result(it.isSuccessful,it.result?.toObject(User::class.java),it.exception?.message))
                }

        }

        fun update(id:String,map:Map<String,Any>,listener: (Result<Boolean>) -> Unit){
                Firebase.firestore.collection("users").document(id).update(map).addOnCompleteListener { task->
                    listener(Result(task.isSuccessful,task.isSuccessful,task.exception?.message))
                }
        }

        //people who are followed by this user
        fun getFollowingOf(uid: String, listener: (Result<List<User>>) -> Unit) {
            Firebase.firestore.collection("users").whereArrayContains("followers",uid).get()
                .addOnCompleteListener {
                    listener(Result(it.isSuccessful,it.result?.toObjects(User::class.java),it.exception?.message))
                }
        }

        fun getFollowersOf(uid: String, listener: (Result<List<User>>) -> Unit) {
            Firebase.firestore.collection("users").whereArrayContains("following",uid).get()
                .addOnCompleteListener {
                    listener(Result(it.isSuccessful,it.result?.toObjects(User::class.java),it.exception?.message))
                }
        }

    }

    fun create(listener:(Task<Void>)->Unit){

        searchText= name?.lowercase(Locale.ROOT) ?:""
        searchKeywords=name?.split(" ")?: emptyList()

        Firebase.firestore.collection("users")
            .document(id!!)
            .set(this)
            .addOnCompleteListener {
                listener(it)
            }
    }

    fun getPosts(
        realtime: Boolean=true,
        listener: (Result<List<Post>>) -> Unit
    ){
        val ref=Firebase.firestore.collection("posts")
            .whereEqualTo("user",id)
            .limit(20)

        if(realtime)
            ref.addSnapshotListener { value, error ->

                if (error!=null){
                    listener(Result(false,null,error.message))
                    return@addSnapshotListener
                }

                if (value!=null)
                    listener(Result(true,value.toObjects(Post::class.java)))


            }
        else
            ref.get().addOnCompleteListener {
                listener(Result(it.isSuccessful,it.result?.toObjects(Post::class.java),it.exception?.message))
            }
    }


    fun follow(uid:String){
        if(!followers?.contains(uid)!!){

            Firebase.firestore.document("users/$id").update("followers",FieldValue.arrayUnion(uid))
                .addOnCompleteListener {
                    if(it.isSuccessful){
                        Firebase.firestore.document("users/$uid").update("following",FieldValue.arrayUnion(id))
                    }
                }

        }
    }

    fun unFollow(uid:String){
        if(followers?.contains(uid)!!){
            Firebase.firestore.document("users/$id").update("followers",FieldValue.arrayRemove(uid))
                .addOnCompleteListener {
                    if(it.isSuccessful){
                        Firebase.firestore.document("users/$uid").update("followers",FieldValue.arrayRemove(id))
                    }
                }
        }
    }

}