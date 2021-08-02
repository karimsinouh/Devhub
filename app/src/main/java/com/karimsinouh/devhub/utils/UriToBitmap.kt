package com.karimsinouh.devhub.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore

fun Uri?.toBitmap(context: Context):Bitmap?{
    return when {
        this==null -> null
        Build.VERSION.SDK_INT < 28 -> MediaStore.Images.Media.getBitmap(context.contentResolver,this)
        Build.VERSION.SDK_INT >= 28  -> {
            val source=ImageDecoder.createSource(context.contentResolver,this)
            ImageDecoder.decodeBitmap(source)
        }
        else ->null
    }
}