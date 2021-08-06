package com.karimsinouh.devhub.ui.viewHashtags

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.karimsinouh.devhub.data.Post
import com.karimsinouh.devhub.data.User
import com.karimsinouh.devhub.utils.ScreenState

class ViewHashtagsViewModel:ViewModel() {

    val state= mutableStateOf(ScreenState.LOADING)
    var hashtag=""

    @ExperimentalPagerApi
    val pagerState by lazy {
        PagerState(4)
    }

    val posts= mutableStateOf<List<Post>>(emptyList())
    val users= mutableStateOf<List<User>>(emptyList())

    var error:String ?=""
    set(value) {
        state.value=if (value!="")
            ScreenState.ERROR
        else
            ScreenState.IDLE
        field=value
    }

    fun loadPosts(hashtag:String){
        Post.getByHashtag(hashtag){
            if(it.isSuccessful){
                posts.value=it.data?: emptyList()
                loadUsers(hashtag)
            }else
                error=it.message
        }
    }

    private fun loadUsers(skill:String){
        User.getBySkill(skill){
            if (it.isSuccessful){
                users.value=it.data?: emptyList()
                state.value=ScreenState.IDLE
            }else{
                error=it.message
            }
        }
    }


}