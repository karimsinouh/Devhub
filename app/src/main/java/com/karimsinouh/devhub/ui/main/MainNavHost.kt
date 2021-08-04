package com.karimsinouh.devhub.ui.main

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import com.google.accompanist.pager.ExperimentalPagerApi
import com.karimsinouh.devhub.ui.createNew.CreateNew
import com.karimsinouh.devhub.ui.editProfile.EditProfile
import com.karimsinouh.devhub.ui.home.Home
import com.karimsinouh.devhub.ui.profile.Profile
import com.karimsinouh.devhub.ui.search.Search
import com.karimsinouh.devhub.ui.notifications.Notifications
import com.karimsinouh.devhub.ui.viewPost.ViewPost
import com.karimsinouh.devhub.ui.viewPost.ViewPostViewModel
import com.karimsinouh.devhub.ui.viewProfile.ViewProfile
import com.karimsinouh.devhub.ui.viewUsersList.ViewUsersList
import com.karimsinouh.devhub.utils.Screen
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalPagerApi
@ExperimentalFoundationApi
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MainNavHost(
    controller:NavHostController,
    vm:MainViewModel
){
    NavHost(
        navController = controller,
        startDestination = Screen.Home.route
    ){

        composable(Screen.Home.route){
            Home(nav = controller, vm = vm)
        }

        composable(Screen.Search.route){
            Search(nav = controller)
        }

        composable(Screen.Profile.route){
            Profile(nav = controller, vm = vm)
        }

        composable(Screen.Notifications.route){
            Notifications(nav = controller, vm = vm)
        }

        composable(Screen.EditProfile.route){
            EditProfile(controller)
        }

        composable(Screen.CreateNew.route){
            CreateNew(nav = controller)
        }

        composable(
            route = Screen.ViewPost.route,
            arguments = listOf(navArgument("postId"){
                type= NavType.StringType
            })
        ){
            val postId=it.arguments?.getString("postId")
            //val viewPostViewModel= hiltViewModel<ViewPostViewModel>()
            ViewPost(nav = controller, postId = postId?:"",)
        }

        composable(
            route=Screen.ViewProfile.route,
            arguments = listOf(navArgument("uid"){type= NavType.StringType})
        ){
            val uid=it.arguments?.getString("uid")!!
            ViewProfile(nav = controller, uid = uid)
        }

        composable(
            route = Screen.ViewUsersList.route,
            arguments = listOf(
                navArgument("uid"){type= NavType.StringType},
                navArgument("action"){type=NavType.IntType}
            )
        ){
            val uid=it.arguments?.getString("uid")!!
            val action=it.arguments?.getInt("action")!!
            ViewUsersList(nav = controller, uid = uid, action =action)
        }

    }
}