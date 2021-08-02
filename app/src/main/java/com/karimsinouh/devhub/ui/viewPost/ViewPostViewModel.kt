package com.karimsinouh.devhub.ui.viewPost

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.karimsinouh.devhub.data.Post
import com.karimsinouh.devhub.data.Reply
import com.karimsinouh.devhub.data.User
import com.karimsinouh.devhub.utils.ScreenState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ViewPostViewModel:ViewModel() {

    val user= mutableStateOf<User?>(null)
    val post= mutableStateOf<Post?>(null)
    val state= mutableStateOf(ScreenState.LOADING)

    val showReplies= mutableStateOf(false)
    val reply= mutableStateOf("")
    val replies= mutableStateOf<List<Reply>>(emptyList())

    private fun loadUser(id:String?=null)=viewModelScope.launch{
        if (id!=null)
            User.get(id,false){
                if(it.isSuccessful)
                    user.value=it.data
            }
    }

    fun loadPost(id:String)=viewModelScope.launch{
        if (post.value==null){
            Post.get(id){
                if (it.isSuccessful) {
                    state.value=ScreenState.IDLE
                    post.value = it.data
                    loadUser(it.data?.user)
                }
            }
            delay(1000)
            loadReplies(id)
        }
    }

    private fun loadReplies(postId:String)=viewModelScope.launch{
        Reply.of(postId){
            if(it.isSuccessful)
                replies.value=it.data?: emptyList()
        }
    }

    fun reply(postId: String){
        if (reply.value.isNotEmpty()){
            Reply.on(postId,reply.value)
            reply.value=""
        }
    }

}