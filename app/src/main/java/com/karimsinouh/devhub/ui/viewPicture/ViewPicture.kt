package com.karimsinouh.devhub.ui.viewPicture

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import com.karimsinouh.devhub.R
import coil.compose.rememberImagePainter
import com.karimsinouh.devhub.utils.Screen
import com.karimsinouh.devhub.utils.customComposables.CenterProgress

@Composable
fun ViewPicture(url:String){

    val painter=rememberImagePainter(
        data = url,
        builder = {
            placeholder(R.drawable.ic_picture_placeholder)
            error(R.drawable.ic_picture_placeholder)
            crossfade(true)
        }
    )

    BoxWithConstraints(
        modifier=Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){

        TransformAbleImage(painter)
    }

}

@Composable
private fun TransformAbleImage(painter:Painter?){
    val scale =remember { mutableStateOf(1f) }
    val rotation = remember { mutableStateOf(0f) }
    val offset = remember { mutableStateOf(Offset.Zero) }
    val state = rememberTransformableState { zoomChange, offsetChange, rotationChange ->
        scale.value *= zoomChange
        rotation.value += rotationChange
        offset.value += offsetChange
    }
    if (painter!=null)
        Image(
            painter=painter,
            modifier = Modifier

                .graphicsLayer(
                    scaleX = scale.value,
                    scaleY = scale.value,
                    rotationZ = rotation.value,
                    translationX = offset.value.x,
                    translationY = offset.value.y
                )
                // add transformable to listen to multitouch transformation events
                // after offset
                .transformable(state = state)
                .fillMaxWidth(),
            contentDescription=""
        )
}