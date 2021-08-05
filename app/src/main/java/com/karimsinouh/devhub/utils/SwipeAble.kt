package com.karimsinouh.devhub.utils

import android.util.Log
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.KeyboardArrowLeft
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp


@Composable
fun SwipeAble(
    onDelete:()->Unit,
    onEdit:()->Unit,
    content: @Composable ()->Unit
){

    val revealed= remember {
        mutableStateOf(false)
    }

    val width= animateDpAsState(if (revealed.value) 100.dp else 0.dp)
    val alpha= animateFloatAsState(if (revealed.value) 1f else 0f )

    Row(
        modifier = Modifier.pointerInput(Unit){

            detectHorizontalDragGestures { change, dragAmount ->

                revealed.value.let { isRevealed->
                    if (dragAmount>10f){
                        if (!isRevealed)
                            revealed.value=true
                    }else if (dragAmount < -10){
                        if (isRevealed)
                            revealed.value=false
                    }
                }

            }

        }
    ){

        if(revealed.value){
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(width.value)
                    .alpha(alpha.value),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ){

                IconButton(
                    onClick = onEdit,
                ) {
                    Icon(Icons.Outlined.Edit,null)
                }

                IconButton(
                    onClick = onDelete,
                ) {
                    Icon(Icons.Outlined.Delete,null)
                }

                IconButton(
                    onClick = { revealed.value=false },
                ) {
                    Icon(Icons.Outlined.KeyboardArrowLeft,null)
                }

            }
        }

        content()



    }

}