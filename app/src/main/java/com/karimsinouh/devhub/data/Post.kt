package com.karimsinouh.devhub.data

import android.os.Parcel
import android.os.Parcelable
import androidx.compose.ui.text.toLowerCase
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.util.*

data class Post(
    val title:String?=null,
    val content:String?=null,
    val hashtags:List<String> ?= emptyList(),
    val upVotes:List<String> ?= emptyList(),
    val downVotes:List<String> ?= emptyList(),
    val votes:Int?=0,
    val type:Int?=0,
    var user:String?=null,
    var userName:String?=null,
    var images:List<String>?= emptyList(),
    var searchText:String?=null,
    var searchKeywords:List<String>?= emptyList(),
    @DocumentId val id:String?=null,
    @ServerTimestamp val date:Date?=null,
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.createStringArrayList(),
        parcel.createStringArrayList(),
        parcel.createStringArrayList(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.createStringArrayList(),
        parcel.readString(),
        parcel.createStringArrayList(),
        parcel.readString(),
        parcel.readSerializable() as? Date
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.apply {
            writeString(title)
            writeString(content)
            writeStringList(hashtags)
            writeStringList(upVotes)
            writeStringList(downVotes)
            writeValue(votes)
            writeValue(type)
            writeString(user)
            writeString(userName)
            writeStringList(images)
            writeString(searchText)
            writeStringList(searchKeywords)
            writeString(id)
            writeSerializable(date)
        }
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Post> {
        override fun createFromParcel(parcel: Parcel): Post {
            return Post(parcel)
        }

        override fun newArray(size: Int): Array<Post?> {
            return arrayOfNulls(size)
        }

        fun getLatestPosts(
            limit: Long,
            realtime:Boolean=true,
            listener: (Result<List<Post>>) -> Unit,
        ) {
            val ref=Firebase.firestore.collection("posts")
                .orderBy("date",Query.Direction.DESCENDING)
                .limit(limit)

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

        fun get(id: String, listener: (Result<Post>) -> Unit) {
            Firebase.firestore.collection("posts").document(id)
                .addSnapshotListener{value,error->
                    if (error!=null){
                        listener(Result(false,null,error.message))
                        return@addSnapshotListener
                    }

                    if (value!=null)
                        listener(Result(true,value.toObject(Post::class.java)))
                }
        }
    }


    fun upload(listener:(Task<DocumentReference>)->Unit){
        val firebaseUser=Firebase.auth.currentUser!!
        val uid=firebaseUser.uid
        val name=firebaseUser.displayName

        user=uid
        userName=name
        searchKeywords=title?.split(" ")
        searchText= title?.lowercase(Locale.getDefault())

        Firebase.firestore.collection("posts").add(this).addOnCompleteListener {
            listener(it)
        }
    }


    fun voteUp(uid:String) {
        //if the user had already up voted before he clicked the button
        val thisUserUpVoted=upVotes?.contains(uid)!!
        val document=Firebase.firestore.collection("posts").document(id!!)
        val action=if (thisUserUpVoted) FieldValue.arrayRemove(uid) else FieldValue.arrayUnion(uid)

        document.update("upVotes",action).addOnCompleteListener {
            if(it.isSuccessful){
                if (downVotes?.contains(uid)!!)
                    document.update("downVotes",FieldValue.arrayRemove(uid))
                document.update("votes",FieldValue.increment(if (thisUserUpVoted) -1 else 1))
            }
        }

    }

    fun voteDown(uid:String) {
        //if the user had already down voted before he clicked the button
        val thisUserDownVoted=downVotes?.contains(uid)!!
        val document=Firebase.firestore.collection("posts").document(id!!)
        val action=if (thisUserDownVoted) FieldValue.arrayRemove(uid) else FieldValue.arrayUnion(uid)

        document.update("downVotes",action).addOnCompleteListener {
            if(it.isSuccessful){
                if (upVotes?.contains(uid)!!)
                    document.update("upVotes",FieldValue.arrayRemove(uid))
                document.update("votes",FieldValue.increment(if (thisUserDownVoted) 1 else -1))
            }
        }

    }

    fun delete(){
        Firebase.firestore.collection("posts").document(id!!)
            .delete().addOnSuccessListener {
                images?.let { imagesList->
                    imagesList.forEach { imageUrl->
                        Firebase.storage.getReferenceFromUrl(imageUrl).delete()
                    }
                }
            }
    }

    fun update(map: HashMap<String, Any>, function: (Boolean) -> Unit) {
        Firebase.firestore.collection("posts").document(id!!)
            .update(map)
            .addOnCompleteListener {
                function(it.isSuccessful)
            }
    }

}