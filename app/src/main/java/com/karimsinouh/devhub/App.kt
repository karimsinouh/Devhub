package com.karimsinouh.devhub

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver

import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.karimsinouh.devhub.data.User


@HiltAndroidApp
class App:Application(),LifecycleObserver{

    override fun onCreate() {
        super.onCreate()
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    private fun onAppBackgrounded() {
        User.makeOnline(false)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private fun onAppForegrounded() {
        User.makeOnline(true)
    }

}