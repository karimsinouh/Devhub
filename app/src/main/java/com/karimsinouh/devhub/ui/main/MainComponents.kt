package com.karimsinouh.devhub.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import com.karimsinouh.devhub.R
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Send
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.karimsinouh.devhub.ui.chat.chatRooms.ChatRoomsActivity
import com.karimsinouh.devhub.ui.theme.Red
import com.karimsinouh.devhub.ui.theme.Shapes
import com.karimsinouh.devhub.utils.Screen

@Composable
fun MainTopBar(
    title:String,
    showBackArrow:Boolean,
    onNavigationIcon: () -> Unit,
    onBackPressed:()->Unit
){
    val context=LocalContext.current
    Column {
        TopAppBar(
            title = {Text(title)},
            navigationIcon = {
                if(showBackArrow)
                    IconButton(onClick = onBackPressed) {
                        Icon(Icons.Default.ArrowBack, null)
                    }
                else
                    IconButton(onClick = onNavigationIcon) {
                        Icon(Icons.Default.Menu, null)
                    }
            },
            contentColor = MaterialTheme.colors.onSurface,
            backgroundColor = MaterialTheme.colors.surface,
            elevation = 0.dp,
            actions = {
                if(!showBackArrow)
                    IconButton(onClick = { ChatRoomsActivity.open(context)}) {
                        Icon(imageVector = Icons.Outlined.Send, contentDescription = null)
                    }
            }
        )
        Divider()
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainDrawer(
    currentRoute:String,
    onNavigate:(route:String)->Unit,
    onLogOut:()->Unit
){
    Column(
        modifier=Modifier.padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        Screen.All.drawerItems.forEach {
            val selected=currentRoute==it.route

            val background=if (selected) MaterialTheme.colors.primary.copy(alpha = 0.1f) else Color.Transparent
            val contentColor=if(selected) MaterialTheme.colors.primary else MaterialTheme.colors.onSurface

            ListItem(
                text = { Text(text=stringResource(id = it.title),color=contentColor) },
                icon = {
                    if(it.icon!=null)
                        Icon(it.icon,null,tint=contentColor)
                    else if (it.drawable!=null)
                        Icon(painterResource(id = it.drawable),null,tint=contentColor)
                },
                modifier = Modifier
                    .clip(Shapes.large)
                    .background(background)
                    .clickable { onNavigate(it.route) }
            )

        }

        ListItem(
            text = { Text("Log out") },
            icon = { Icon(painter = painterResource(id = R.drawable.ic_logout), null) },
            modifier = Modifier
                .clip(Shapes.large)
                .clickable(onClick = onLogOut)
        )

    }
}

@Composable
fun NotificationsActiveIcon(){
    Box(contentAlignment = Alignment.Center) {
        Icon(imageVector = Icons.Outlined.Notifications, contentDescription = null)
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(Red)
                .align(Alignment.TopEnd),
        )
    }
}