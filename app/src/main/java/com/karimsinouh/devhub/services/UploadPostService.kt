package com.karimsinouh.devhub.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.Service
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.karimsinouh.devhub.R
import com.karimsinouh.devhub.data.Post
import com.karimsinouh.devhub.utils.Upload
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class UploadPostService: Service() {

    companion object{
        const val NOTIFICATION_CHANNEL_ID="uploadPostNotification"
    }

    override fun onBind(intent: Intent?): IBinder? =null

    @Inject
    lateinit var manager:NotificationManager

    @Inject
    lateinit var notification:NotificationCompat.Builder

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.O)
            createNotificationChannel()

        val pictures=intent?.getParcelableArrayListExtra<Uri>("bitmaps") ?: emptyList<Uri>()
        val post=intent?.getParcelableExtra<Post>("post")!!

        notification.setContentText(post.title?:"")

        if (pictures.isNotEmpty()){
            // upload images first
            Upload.multipleImages(pictures){
                if (it.isSuccessful){
                    post.images=it.data
                    uploadPost(post)
                }else{
                    onError(it.message)
                }
            }
        }else{
            uploadPost(post)
        }

        startForeground(1,notification.build())
        return START_STICKY
    }

    private fun onError(message: String?) {
        stopSelf()
        notification.setContentTitle("Something went wrong")
            .setContentText(message?:"")
            .setSmallIcon(R.drawable.ic_error)
        manager.notify(2,notification.build())
    }
    private fun onSuccess() {
        stopSelf()
        notification
            .setContentTitle("Your post has been successfully uploaded!")
            .setSmallIcon(R.drawable.ic_done)
        manager.notify(2,notification.build())
    }

    private fun uploadPost(post: Post) {
        post.upload {
            if (it.isSuccessful)
                onSuccess()
            else
                onError(it.exception?.message)
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun createNotificationChannel(){
        val channel=NotificationChannel(NOTIFICATION_CHANNEL_ID,"Upload Post Channel",IMPORTANCE_HIGH)
        manager.createNotificationChannel(channel)
    }

}