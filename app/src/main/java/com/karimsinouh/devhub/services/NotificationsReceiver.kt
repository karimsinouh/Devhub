package com.karimsinouh.devhub.services

import android.app.NotificationChannel
import android.app.NotificationManager
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
        User.updateToken(Firebase.auth.currentUser?.uid!!,token)
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

        val randomId= Random.nextInt()
        manager.notify(randomId,notificationBuilder.build())

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