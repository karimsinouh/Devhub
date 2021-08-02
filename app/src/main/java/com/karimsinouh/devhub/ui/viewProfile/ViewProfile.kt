package com.karimsinouh.devhub.ui.viewProfile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.karimsinouh.devhub.ui.items.PostItem
import com.karimsinouh.devhub.ui.profile.SkillsSection
import com.karimsinouh.devhub.ui.profile.UserInfoSection
import com.karimsinouh.devhub.utils.Screen
import com.karimsinouh.devhub.utils.ScreenState
import com.karimsinouh.devhub.utils.customComposables.CenterProgress

@Composable
fun ViewProfile(
    nav:NavController,
    uid:String,
    vm:ViewProfileViewModel=viewModel(),
){
    SideEffect {
        vm.loadUser(uid)
    }


    vm.state.value.let {
        when(it){

            ScreenState.LOADING-> CenterProgress()

            ScreenState.IDLE->{
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        UserInfoSection(
                            nav=nav,
                            user = vm.user.value!!,
                            postCount = vm.posts.value.size
                        )
                    }

                    item {
                        SkillsSection(vm.user.value?.skills?: emptyList())
                    }

                    items(vm.posts.value){item->
                        PostItem(item) {
                            nav.navigate(Screen.ViewPost.constructRoute(item.id!!))
                        }
                    }

                }
            }
        }
    }

}