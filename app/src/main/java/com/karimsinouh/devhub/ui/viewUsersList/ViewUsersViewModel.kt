package com.karimsinouh.devhub.ui.viewUsersList

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.karimsinouh.devhub.data.Result
import com.karimsinouh.devhub.data.User
import com.karimsinouh.devhub.utils.ScreenState

class ViewUsersViewModel:ViewModel(){

    val users= mutableStateOf<List<User>>(emptyList())
    val state= mutableStateOf(ScreenState.LOADING)
    var error:String=""
    set(value) {
        if (value!="")
            state.value=ScreenState.ERROR
        else
            state.value=ScreenState.IDLE
        field=value
    }

    fun loadUsers(actionType:Int,uid:String){
        when(actionType){

            VIEW_FOLLOWING->
                User.getFollowingOf(uid=uid){
                    if (it.isSuccessful){
                        state.value=ScreenState.IDLE
                        users.value=it.data!!
                    }else
                        error=it.message?:""

                }


            VIEW_FOLLOWERS->
                User.getFollowersOf(uid=uid){
                    if (it.isSuccessful){
                        state.value=ScreenState.IDLE
                        users.value=it.data!!
                    }else
                        error=it.message?:""

                }

        }
    }


}