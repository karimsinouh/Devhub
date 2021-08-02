package com.karimsinouh.devhub.ui.viewProfile

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.karimsinouh.devhub.data.Post
import com.karimsinouh.devhub.data.User
import com.karimsinouh.devhub.utils.ScreenState
import kotlinx.coroutines.launch

class ViewProfileViewModel:ViewModel() {
    val user= mutableStateOf<User?>(null)
    val posts= mutableStateOf<List<Post>>(emptyList())
    val state= mutableStateOf(ScreenState.LOADING)

    var error:String ?=""
    set(value){
        if (value!="")
            state.value=ScreenState.ERROR
        else
            state.value=ScreenState.IDLE
        field=value
    }

    fun loadUser(uid:String)=viewModelScope.launch{

        if(user.value==null)
            User.get(uid,true){
                if (it.isSuccessful){
                    state.value=ScreenState.IDLE
                    user.value=it.data
                    //get posts
                    it.data?.getPosts { postsTask->
                        if (postsTask.isSuccessful)
                            posts.value=postsTask.data ?: emptyList()
                        else
                            error=postsTask.message
                    }
                }else
                    error=it.message
            }

    }


}