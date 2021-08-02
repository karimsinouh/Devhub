package com.karimsinouh.devhub.ui.search

import androidx.compose.ui.text.toLowerCase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.karimsinouh.devhub.data.Post
import com.karimsinouh.devhub.data.Result
import com.karimsinouh.devhub.data.User
import dagger.hilt.android.scopes.ViewModelScoped
import java.util.*
import javax.inject.Inject

class SearchRepository{

    companion object{
        const val TYPE_QUESTION=0
        const val TYPE_TUTORIAL=1
        const val TYPE_ANNOUNCEMENT=2
        const val TYPE_HASHTAG=3
        const val TYPE_USER=4
        const val TYPE_USER_BY_SKILL=5

    }

    private val firestore=Firebase.firestore

    fun posts(
        query:String,
        type:Int,
        listener:(Result<List<Post>>)->Unit
    ){
        val ref=firestore.collection("posts")

        if(type== TYPE_HASHTAG)
            postsByHashTag(query,listener = listener)
        else
            ref.whereEqualTo("searchText",query)
                .whereEqualTo("type",type)
                .get()
                .addOnCompleteListener { searchByText->
            if (searchByText.isSuccessful){
               val result1=searchByText.result?.toObjects(Post::class.java) ?: emptyList()

                if (result1.isNotEmpty()){
                    listener(Result(true,result1))
                }else{

                    ref
                        .whereArrayContains("searchKeywords",query)
                        .whereEqualTo("type",type)
                        .get()
                        .addOnCompleteListener {searchByKeywords->

                        if(searchByKeywords.isSuccessful){
                            val result2=searchByKeywords.result?.toObjects(Post::class.java)?: emptyList()
                            listener( Result(result2.isNotEmpty(),result2,"No results were found") )
                        }else{
                            listener(Result(false,null,searchByKeywords.exception?.message))
                        }

                    }

                }

            }else{
                listener(Result(false,null,searchByText.exception?.message))
            }
        }

    }

    private fun postsByHashTag(
        tag:String,
        listener:(Result<List<Post>>)->Unit
    ){
        firestore.collection("posts")
            .whereArrayContains("hashtags", tag.lowercase(Locale.getDefault()))
            .get()
            .addOnCompleteListener {
                if(it.isSuccessful){
                    val result=it.result?.toObjects(Post::class.java)?: emptyList()
                    listener(Result(result.isNotEmpty(),result,"no results were found"))
                }else{
                 listener(Result(false,null,it.exception?.message))
                }
            }
    }


    fun users(
        query:String,
        type: Int,
        listener:(Result<List<User>>)->Unit
    ){

        val ref=if(type== TYPE_USER)
            firestore.collection("users").whereEqualTo("searchText",query)
        else
            firestore.collection("users").whereArrayContains("skills",query)


        ref.get().addOnCompleteListener {
            if(it.isSuccessful){
                val result=it.result?.toObjects(User::class.java)?: emptyList()
                listener(Result(result.isNotEmpty(),result,"no results were found"))
            }else{
                listener(Result(false,null,it.exception?.message))
            }
        }

    }

}