package com.karimsinouh.devhub.ui.viewUsersList

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.karimsinouh.devhub.data.User
import com.karimsinouh.devhub.ui.items.UserItem
import com.karimsinouh.devhub.utils.Screen
import com.karimsinouh.devhub.utils.ScreenState
import com.karimsinouh.devhub.utils.customComposables.CenterProgress

const val VIEW_FOLLOWERS=1
const val VIEW_FOLLOWING=2

@Composable
fun ViewUsersList(
    nav:NavController,
    uid:String,
    action:Int,
    vm:ViewUsersViewModel= viewModel()
){

    SideEffect {
        vm.loadUsers(action,uid)
    }

    when(vm.state.value){
        ScreenState.LOADING -> CenterProgress()


        ScreenState.IDLE -> UsersList(list = vm.users.value){
            nav.navigate(Screen.ViewProfile.constructRoute(it.id!!))
        }


        ScreenState.ERROR -> {}


        ScreenState.DONE -> { }
    }

}

@Composable
fun UsersList(
    list:List<User>,
    onClick:(User)->Unit
){
    LazyColumn(
        modifier=Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ){
        items(list){item->
            UserItem(item) {
                onClick(item)
            }
            Divider()
        }
    }
}