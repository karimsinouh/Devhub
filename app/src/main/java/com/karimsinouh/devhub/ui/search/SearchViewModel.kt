package com.karimsinouh.devhub.ui.search

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.karimsinouh.devhub.data.Post
import com.karimsinouh.devhub.data.User
import com.karimsinouh.devhub.utils.ScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchViewModel: ViewModel() {

    private val repo=SearchRepository()

    val state= mutableStateOf(ScreenState.IDLE)
    var error:String=""
    set(value) {
        state.value=if (value!="")
            ScreenState.ERROR
        else
            ScreenState.IDLE
        field=value
    }

    val query= mutableStateOf("")

    val searchType= mutableStateOf(SearchRepository.TYPE_QUESTION)
    val searchTypeVisible= mutableStateOf(false)

    val users= mutableStateOf<List<User>>(emptyList())
    val posts= mutableStateOf<List<Post>>(emptyList())

    fun search()=viewModelScope.launch{
        state.value=ScreenState.LOADING

        if (query.value!=""){

            val forUsers=searchType.value==SearchRepository.TYPE_USER || searchType.value==SearchRepository.TYPE_USER_BY_SKILL

            if(forUsers)
                searchForUsers()
            else
                searchForPosts()

        }

    }

    private fun searchForPosts(){
        repo.posts(
            query = query.value,
            type = searchType.value,
        ){
            if(it.isSuccessful){
                state.value=ScreenState.IDLE
                posts.value=it.data?: emptyList()
            }else
                error=it.message?:""

        }
    }

    private fun searchForUsers(){
        repo.users(
            query = query.value,
            type = searchType.value
        ){
            if(it.isSuccessful){
                state.value=ScreenState.IDLE
                users.value=it.data ?: emptyList()
            }else
                error=it.message?:""

        }
    }

}