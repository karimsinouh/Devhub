package com.karimsinouh.devhub.ui.viewPost

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.karimsinouh.devhub.data.Notification
import com.karimsinouh.devhub.data.Post
import com.karimsinouh.devhub.data.Reply
import com.karimsinouh.devhub.data.User
import com.karimsinouh.devhub.ui.notifications.NotificationsRepository
import com.karimsinouh.devhub.utils.ScreenState
import io.ktor.client.*
import io.ktor.client.features.json.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ViewPostViewModel: ViewModel() {

    //some really weird issue happened while I was trying to make this view model injectable :(

    private val httpClient by lazy {
        HttpClient{ install(JsonFeature) }
    }
    private val notificationsRepository by lazy {
        NotificationsRepository(httpClient)
    }

    val currentUser by lazy { Firebase.auth.currentUser!! }

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

    fun reply(){

        val postId=post.value?.id!!

        if (reply.value.isNotEmpty()){
            Reply.on(postId,reply.value,currentUser)

            val notification=Notification(
                "${currentUser.displayName} Replied to your post",
                post.value?.title!!,
                Notification.TYPE_REPLY,
                postId,
                post.value?.user!!
            )
            notificationsRepository.sendNotification(notification.asData(user.value?.token))
            reply.value=""
        }
    }

    fun onUpVote() {

        val notification=Notification(
            title = "Congrats! someone up voted your post.",
            content = post.value?.title,
            type=Notification.TYPE_UP_VOTE,
            action = post.value?.id!!,
            receiverId = user.value?.id!!
        )

        notificationsRepository.sendNotification(notification.asData(user.value?.token))
    }

    fun onDownVote() {
        val notification=Notification(
            title = "Someone down voted your post :(",
            content = post.value?.title,
            type=Notification.TYPE_DOWN_VOTE,
            action = post.value?.id!!,
            receiverId = user.value?.id!!
        )
        notificationsRepository.sendNotification(notification.asData(user.value?.token))
    }

}

