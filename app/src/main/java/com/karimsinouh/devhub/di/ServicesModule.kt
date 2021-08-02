package com.karimsinouh.devhub.di

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import com.karimsinouh.devhub.R
import com.karimsinouh.devhub.services.UploadPostService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ServiceComponent::class)
object ServicesModule {

    @Provides
    fun notificationManager(@ApplicationContext c:Context)=c.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    @Provides
    fun postNotification(@ApplicationContext c:Context)=
        NotificationCompat.Builder(c,UploadPostService.NOTIFICATION_CHANNEL_ID)
            .setContentTitle("Uploading your post")
            .setSmallIcon(R.drawable.ic_upload)

}