package com.karimsinouh.devhub.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.karimsinouh.devhub.R
import com.karimsinouh.devhub.data.Notification
import com.karimsinouh.devhub.data.User
import com.karimsinouh.devhub.ui.chat.chat.ChatActivity
import com.karimsinouh.devhub.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.random.Random


@AndroidEntryPoint
class NotificationsReceiver:FirebaseMessagingService() {

    companion object{
        private const val NOTIFICATION_CHANNEL_ID="11101"
    }

    @Inject lateinit var manager:NotificationManager

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        User.updateToken(Firebase.auth.currentUser?.uid!!,token){}
    }

    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            createNotificationChannel()

        val notification= Notification.fromMap(p0.data)

        val notificationBuilder=NotificationCompat
            .Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle(notification.title)
            .setContentText(notification.content)
            .setSmallIcon(R.drawable.ic_trophy)
            .setAutoCancel(true)


        if (notification.type==Notification.TYPE_MESSAGE)
            notificationBuilder.setContentIntent(getPendingIntentForChat(notification.action!!))
        else
            notificationBuilder.setContentIntent(getPendingIntentForPost(notification.type!!,notification.action!!))

        val randomId= Random.nextInt()
        manager.notify(randomId,notificationBuilder.build())

    }

    private fun getPendingIntentForChat(_action:String):PendingIntent{
        val action=_action.split("}")
        val userId=action[0]
        val chatRoom=action[1]
        val intent=Intent(this,ChatActivity::class.java).apply {
            putExtra("userId",userId)
            putExtra("chatRoomId",chatRoom)
        }
        return PendingIntent.getActivity(this,1,intent,PendingIntent.FLAG_UPDATE_CURRENT)
    }

    private fun getPendingIntentForPost(type:Int,actionId:String):PendingIntent{
        val intent=Intent(this,MainActivity::class.java).apply {
            action=type.toString()
            putExtra("actionId",actionId)
        }
        return PendingIntent.getActivity(this,2,intent,PendingIntent.FLAG_UPDATE_CURRENT)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun createNotificationChannel(){
        val channel= NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            "Notifications channel",
            NotificationManager.IMPORTANCE_HIGH
        )
        manager.createNotificationChannel(channel)
    }

}