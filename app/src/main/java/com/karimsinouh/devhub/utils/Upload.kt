package com.karimsinouh.devhub.utils

import android.net.Uri
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.karimsinouh.devhub.data.Result

object Upload {

    const val PROFILE_PICTURES="profilePictures"
    const val POSTS_PICTURES="postsPictures"

    fun image(
        path:String,
        picture: Uri,
        fileName:String?="${System.currentTimeMillis()}.jpg",
        listener:(Result<String>)->Unit
    ){
        val ref=Firebase.storage.getReference(path).child(fileName!!)

        val uploadTask=ref.putFile(picture)

        val urlTask=uploadTask.continueWithTask {
            if (!it.isSuccessful){
                it.exception?.let {
                    throw  it
                }
                listener(Result(false,null,it.exception?.message))
            }
            ref.downloadUrl
        }.addOnCompleteListener {
            listener(Result(it.isSuccessful,it.result.toString(),it.exception?.message))
        }

    }

    fun multipleImages(
        pictures:List<Uri>,
        listener: (Result<List<String>>) -> Unit
    ){
        val urls= mutableListOf<String>()
        val size=pictures.size
        val uid=Firebase.auth.currentUser?.uid!!

        for (i in 0 until size){

            image("$POSTS_PICTURES/$uid",pictures[i]){
                if (it.isSuccessful) {
                    urls.add(it.data!!)
                    if ((i+1)==size){
                        listener(Result(true,urls))
                    }
                }
                else
                    listener(Result(false,null,it.message))
            }

        }
    }

}