package com.karimsinouh.devhub.ui.editPost

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.karimsinouh.devhub.data.Post
import com.karimsinouh.devhub.utils.ScreenState
import com.karimsinouh.devhub.utils.isValidContent
import com.karimsinouh.devhub.utils.isValidTitle
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.HashMap

class EditPostViewModel:ViewModel() {

    val state by lazy{
        mutableStateOf(ScreenState.LOADING)
    }

    val post by lazy { mutableStateOf<Post?>(null) }

    var error:String ?=""
    set(value) {

        state.value=if(value=="")
            ScreenState.IDLE
        else
            ScreenState.ERROR
        field=value
    }

    val title by lazy {
        mutableStateOf("")
    }

    val content by lazy {
        mutableStateOf("")
    }

    val hashtags by lazy {
        mutableStateOf("")
    }

    fun update(){

        if (!isValidTitle(title.value)){
            error="Please enter a valid title"
            return
        }

        if (!isValidContent(content.value)){
            error="Please enter a valid content"
            return
        }

        val map=HashMap<String,Any>()

        map["title"]=title.value
        map["content"]=content.value
        map["hashtags"]=getHashtags(hashtags.value)

        post.value?.update(map){successful->
            if (successful)
                state.value=ScreenState.DONE
            else
                error="unfortunately something went wrong in the database"

        }
    }

    fun loadPost(id:String)=viewModelScope.launch{
        delay(500)
        if(post.value==null)
            Post.get(id){
                if (it.isSuccessful){
                    val _post=it.data!!
                    post.value=_post
                    state.value=ScreenState.IDLE

                    title.value=_post.title!!
                    content.value=_post.content!!
                    hashtags.value=_post.hashtags?.toHashtags() ?: ""

                }else
                    error=it.message

            }
    }

    fun getHashtags(value:String):List<String>{
        val list= value.lowercase(Locale.getDefault()).split(",").toMutableList()
        list.remove("")
        list.remove(" ")
        list.remove("  ")
        return list
    }


    private fun List<String>.toHashtags():String{
        val str=StringBuilder()
        forEachIndexed {i,it->
            str.append(it + if ((i+1)!=size) "," else "")
        }
        return str.toString()
    }


}