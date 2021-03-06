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
    val picture:String?="https://upload.wikimedia.org/wikipedia/commons/7/7c/Profile_avatar_placeholder_large.png",
    val followers:List<String>?= emptyList(),
    val following:List<String>?= emptyList(),
    var searchText:String?=null,
    var searchKeywords:List<String>?= emptyList(),
    val online:Boolean?=false,
    val github:String?="",
    val behance:String?="",
    val dribble:String?="",
    val token:String?=null,
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

                    if (value!=null){

                        val user=value.toObject(User::class.java)

                        if (user==null)
                            listener(Result(false,null,"This user might be deleted"))
                        else
                            listener(Result(true,user))

                    }
                }
            else
                ref.get().addOnCompleteListener {

                    val user=it.result?.toObject(User::class.java)

                    if (it.isSuccessful){
                        if (user==null)
                            listener(Result(false,null,"This user might be deleted"))
                        else
                            listener(Result(it.isSuccessful,user,it.exception?.message))

                    }else{
                        listener(Result(false,null,it.exception?.message))
                    }

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

        fun updateToken(uid:String,token: String,listener:(isSuccessful:Boolean)->Unit){
            Firebase.firestore.collection("users").document(uid)
                .update("token",token)
                .addOnCompleteListener { task->
                    listener(task.isSuccessful)
                }
        }

        fun getBySkill(skill:String,listener: (Result<List<User>>) -> Unit){
            Firebase.firestore.collection("users").whereArrayContains("skills",skill)
                .get().addOnCompleteListener {
                    val result=Result(it.isSuccessful,it.result?.toObjects(User::class.java),it.exception?.message)
                    listener(result)
                }
        }

        fun makeOnline(value:Boolean){
            val uid=Firebase.auth.currentUser?.uid!!
            Firebase.firestore.collection("users").document(uid).update("online",value)
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