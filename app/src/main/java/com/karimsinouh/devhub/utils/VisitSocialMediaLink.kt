package com.karimsinouh.devhub.utils

import android.content.Context
import android.content.Intent
import android.net.Uri

object VisitSocialMediaLink {

    fun github(context: Context,username:String){
        val url="https://github.com/$username"
        openBrowser(context,url)
    }

    fun behance(context: Context,username:String){
        val url="https://www.behance.net/$username"
        openBrowser(context,url)
    }

    fun dribble(context: Context,username: String){
        val url="https://dribbble.com/$username"
        openBrowser(context,url)
    }

    private fun openBrowser(context: Context,link:String){
        val intent= Intent(Intent.ACTION_VIEW, Uri.parse(link))
        context.startActivity(intent)
    }

}