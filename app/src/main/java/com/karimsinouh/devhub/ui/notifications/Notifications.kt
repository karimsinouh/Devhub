package com.karimsinouh.devhub.ui.notifications

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.karimsinouh.devhub.data.Notification
import com.karimsinouh.devhub.ui.chat.chat.ChatActivity
import com.karimsinouh.devhub.ui.items.NotificationItem
import com.karimsinouh.devhub.ui.main.MainViewModel
import com.karimsinouh.devhub.utils.Screen
import com.karimsinouh.devhub.utils.ScreenState
import com.karimsinouh.devhub.utils.customComposables.CenterProgress
import com.karimsinouh.devhub.utils.customComposables.MessageScreen

@Composable
fun Notifications(
    nav:NavController,
    vm:MainViewModel,
) {

    val context= LocalContext.current

    when(vm.notificationsState.value){

        ScreenState.LOADING-> CenterProgress()

        ScreenState.IDLE->{

            val notifications=vm.notifications.value

            if(notifications.isEmpty())
                MessageScreen(
                    title = "It's a bit empty here",
                    message = "You haven't received any notifications yet"
                )
            else
                NotificationsList(list = notifications){
                    it.makeAsSeen()
                    onNotificationClick(it,nav,context)
                }

        }

    }

}

private fun onNotificationClick(notification:Notification,nav:NavController,context: Context){


    when(notification.type){

        Notification.TYPE_DOWN_VOTE->
            nav.navigate(Screen.ViewPost.constructRoute(notification.action!!))

        Notification.TYPE_UP_VOTE->
            nav.navigate(Screen.ViewPost.constructRoute(notification.action!!))

        Notification.TYPE_REPLY->
            nav.navigate(Screen.ViewPost.constructRoute(notification.action!!))

        Notification.TYPE_FOLLOW->
            nav.navigate(Screen.ViewProfile.constructRoute(notification.action!!))

        Notification.TYPE_MESSAGE->{
            val action=notification.action?.split("}")!!
            val userId=action[0]
            val chatRoom=action[1]
            ChatActivity.open(
                c=context,
                chatRoomId = chatRoom,
                userId = userId,
            )
        }

    }
}

@Composable
private fun NotificationsList(
    list:List<Notification>,
    onClick:(item:Notification)->Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ){
        items(items=list,key= { it.id!! }){item->
            NotificationItem(notification = item) {
                onClick(item)
            }
        }
    }
}
