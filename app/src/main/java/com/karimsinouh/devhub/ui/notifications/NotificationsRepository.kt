package com.karimsinouh.devhub.ui.notifications

import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.karimsinouh.devhub.data.Notification
import com.karimsinouh.devhub.data.NotificationData
import com.karimsinouh.devhub.data.User
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationsRepository @Inject constructor(
    private val client:HttpClient
) {

    companion object{
        private const val SERVER_KEY="AAAAsEi5JRM:APA91bEtqJJzkwTczZJK-CfmTKHofCKOvQv9b5hI41hvwoYHkCfvhBuD7TUGdzLeBYAT9ULgKtI39SGFw51Govnc0iDjmMWBUq2ozvwjGY2aKY_yzFRBDJP6SNngfnUoCTN9twaB8ilQ"
        const val CONTENT_TYPE = "application/json"
    }

    fun sendNotification(notification: NotificationData){

        val currentUid=Firebase.auth.currentUser?.uid?:"none"

        if(currentUid==notification.data.receiverId)
            return

        if(notification.to!=null)
            storeIt(notification)
        else
            User.get(notification.data.receiverId) {
                if(it.isSuccessful && it.data?.token!=null){
                    notification.to=it.data.token
                    storeIt(notification)
                }
            }
    }

    private fun storeIt(notification: NotificationData){
        notification.storeInDatabase { isSuccessful ->
            if(isSuccessful){
                notification.push()
            }
        }
    }

    private fun NotificationData.push()= CoroutineScope(Dispatchers.IO).launch{
        val response=client.post<HttpResponse>("https://fcm.googleapis.com/fcm/send"){
            headers {
                header("Authorization","key=$SERVER_KEY")
                header("Content-Type", CONTENT_TYPE)
            }
            body=this@push
        }

        Log.d("NotificationsRepository",response.status.description)
        Log.d("NotificationsRepository",response.status.value.toString())
    }



}