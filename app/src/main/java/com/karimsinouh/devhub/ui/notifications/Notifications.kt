package com.karimsinouh.devhub.ui.notifications

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.karimsinouh.devhub.data.Notification
import com.karimsinouh.devhub.ui.items.NotificationItem
import com.karimsinouh.devhub.ui.main.MainViewModel
import com.karimsinouh.devhub.utils.ScreenState
import com.karimsinouh.devhub.utils.customComposables.CenterProgress
import com.karimsinouh.devhub.utils.customComposables.MessageScreen

@Composable
fun Notifications(
    nav:NavController,
    vm:MainViewModel,
) {

    when(vm.notificationsState.value){

        ScreenState.LOADING-> CenterProgress()

        ScreenState.IDLE->{
            val notifications=vm.notifications.value
            if(notifications.isEmpty()){
                MessageScreen(
                    title = "It's a bit empty here",
                    message = "You haven't received any notifications yet"
                )
            }else{
                NotificationsList(list = notifications)
            }
        }

    }

}

@Composable
private fun NotificationsList(list:List<Notification>) {
    LazyColumn(
        contentPadding = PaddingValues(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ){
        items(items=list,key= { it.id!! }){item->
            NotificationItem(notification = item) {
                item.makeAsSeen()
            }
        }
    }
}
