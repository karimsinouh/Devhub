package com.karimsinouh.devhub.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.navArgument
import com.karimsinouh.devhub.data.Post
import com.karimsinouh.devhub.ui.items.PostItem
import com.karimsinouh.devhub.ui.main.MainViewModel
import com.karimsinouh.devhub.utils.Screen
import com.karimsinouh.devhub.utils.ScreenState
import com.karimsinouh.devhub.utils.customComposables.CenterProgress

@Composable
fun Home(
    nav:NavController,
    vm:MainViewModel
){

    when(vm.homeState.value){

        ScreenState.IDLE-> PostsList(vm.posts.value) {
            nav.navigate(Screen.ViewPost.constructRoute(it.id!!))
        }

        ScreenState.LOADING-> CenterProgress()

    }

}

@Composable
fun PostsList(
    items:List<Post>,
    onClick:(Post)->Unit
){
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(items){item->
            PostItem(post = item){
                onClick(item)
            }
        }
    }
}

