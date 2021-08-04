package com.karimsinouh.devhub.ui.main


import androidx.compose.material.DrawerState
import androidx.compose.material.DrawerValue
import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.karimsinouh.devhub.data.Notification
import com.karimsinouh.devhub.data.Post
import com.karimsinouh.devhub.data.User
import com.karimsinouh.devhub.utils.Screen
import com.karimsinouh.devhub.utils.ScreenState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainViewModel:ViewModel() {

    var editProfileHasBeenOpened=false
    val currentUid= Firebase.auth.currentUser?.uid!!

    init {

        viewModelScope.launch {
            delay(500)
            loadPosts().join()

            delay(500)
            loadUser().join()

            delay(1000)
            loadNotifications().join()
        }


    }

    val error= mutableStateOf<String?>(null)
    val currentScreen= mutableStateOf<Screen>(Screen.Home)

    //profile
    val user= mutableStateOf<User?>(null)
    val userPosts= mutableStateOf<List<Post>>(emptyList())
    val profileState= mutableStateOf(ScreenState.LOADING)

    //home
    val homeState= mutableStateOf(ScreenState.LOADING)
    val posts= mutableStateOf<List<Post>>(emptyList())

    //notifications
    val notifications= mutableStateOf<List<Notification>>(emptyList())
    val notificationsState= mutableStateOf(ScreenState.LOADING)

    val scaffoldState=ScaffoldState(
        DrawerState(DrawerValue.Closed),
        SnackbarHostState()
    )

    fun shouldShowBottomBar(route:String):Boolean {
        Screen.All.bottomNavItems.forEach {
            if (it.route==route)
                return true
        }
        return false
    }

    private fun loadNotifications()=viewModelScope.launch{
        Notification.get(currentUid){
            if(it.isSuccessful){
                notifications.value=it.data?: emptyList()
                notificationsState.value=ScreenState.IDLE
            }else{
                error.value=it.message
            }
        }
    }

    private fun loadPosts()=viewModelScope.launch{
        Post.getLatestPosts(20){
            if(it.isSuccessful){
                posts.value=it.data?: emptyList()
                homeState.value=ScreenState.IDLE
            }else{
                error.value=it.message
            }
        }
    }

    private fun loadUser()= viewModelScope.launch {
        User.get(currentUid, true){
            if (it.isSuccessful) {
                profileState.value=ScreenState.IDLE
                user.value = it.data!!

                it.data.getPosts{userPostsTask->
                    if (userPostsTask.isSuccessful)
                        userPosts.value=userPostsTask.data?: emptyList()
                    else
                        error.value=userPostsTask.message
                }
            }
            else
                error.value=it.message
        }
    }

}