package com.karimsinouh.devhub.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.karimsinouh.devhub.R
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.karimsinouh.devhub.data.Notification
import com.karimsinouh.devhub.ui.authentication.AuthenticationActivity
import com.karimsinouh.devhub.ui.theme.DevhubTheme
import com.karimsinouh.devhub.ui.theme.DrawerShape
import com.karimsinouh.devhub.ui.theme.Red
import com.karimsinouh.devhub.utils.Screen
import dagger.hilt.internal.GeneratedComponent
import kotlinx.coroutines.*

class MainActivity : ComponentActivity(){

    override fun onStart() {
        super.onStart()
        if(Firebase.auth.currentUser==null){
            startActivity(Intent(this,AuthenticationActivity::class.java))
            finish()
        }
    }

    private val vm by viewModels<MainViewModel>()
    private lateinit var navController:NavHostController
    private lateinit var scope:CoroutineScope

    @ExperimentalAnimationApi
    @OptIn(ExperimentalPagerApi::class)
    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Devhub)
        setContent {

            navController= rememberAnimatedNavController()
            scope= rememberCoroutineScope()

            OnDestinationChanges()

            DevhubTheme {

                window.statusBarColor=MaterialTheme.colors.surface.toArgb()
                val shouldShowBottomBar=vm.shouldShowBottomBar(vm.currentScreen.value.route)

                Scaffold(
                    scaffoldState= vm.scaffoldState,
                    topBar = { TopBarSection() },
                    bottomBar = { if (shouldShowBottomBar) BottomBarSection() },
                    backgroundColor = MaterialTheme.colors.background,
                    floatingActionButton = {
                        if(shouldShowBottomBar)
                            FloatingActionButton(
                                onClick = {
                                          navController.navigate(Screen.CreateNew.route)
                                },
                                backgroundColor = MaterialTheme.colors.primary,
                                contentColor = MaterialTheme.colors.onPrimary
                            ) {
                                Icon(imageVector = Icons.Default.Add,null)
                            }
                    },
                    isFloatingActionButtonDocked = true,
                    floatingActionButtonPosition = FabPosition.Center,
                    drawerContent = {Drawer()},
                    drawerShape = DrawerShape,
                    drawerBackgroundColor = MaterialTheme.colors.surface,
                    drawerElevation = 0.dp,
                    drawerGesturesEnabled = true,
                    drawerScrimColor = MaterialTheme.colors.onSurface.copy(alpha=0.2f)
                ) {


                    MainNavHost(controller = navController, vm = vm)

                    RedirectUser()

                }
            }

        }

    }

    @Composable
    private fun RedirectUser() {
        LaunchedEffect(true){
            delay(100)
            intent.action?.let { action->

                val actionId=intent.getStringExtra("actionId")?:""

                when(action){

                    "completeProfile"->{
                        if (!vm.editProfileHasBeenOpened){
                            vm.editProfileHasBeenOpened=true
                            navController.navigate(Screen.EditProfile.route)
                        }
                    }

                    Notification.TYPE_DOWN_VOTE.toString()->
                        navController.navigate(Screen.ViewPost.constructRoute(actionId))

                    Notification.TYPE_UP_VOTE.toString()->
                        navController.navigate(Screen.ViewPost.constructRoute(actionId))

                    Notification.TYPE_REPLY.toString()->
                        navController.navigate(Screen.ViewPost.constructRoute(actionId))

                    Notification.TYPE_FOLLOW.toString()->
                        navController.navigate(Screen.ViewProfile.constructRoute(actionId))

                }

            }
        }
    }

    @Composable
    fun OnDestinationChanges(){
        val map=Screen.All.map()
        navController.addOnDestinationChangedListener { _, destination, _ ->
            destination.route?.let{
                vm.currentScreen.value=map[it]?: Screen.Home
            }
        }
    }

    @Composable
    private fun TopBarSection(){
        val shouldShowBackArrow= !vm.shouldShowBottomBar(vm.currentScreen.value.route)
        MainTopBar(
            title= stringResource(id = vm.currentScreen.value.title),
            showBackArrow=shouldShowBackArrow,
            onNavigationIcon = { toggleDrawer() },
            onBackPressed = { navController.popBackStack() },
        )
    }


    private fun toggleDrawer()=scope.launch {
            if(vm.scaffoldState.drawerState.isOpen)
                vm.scaffoldState.drawerState.close()
            else
                vm.scaffoldState.drawerState.open()
    }



    @Composable
    private fun BottomBarSection(){

        val selectedScreenRoute = vm.currentScreen.value.route

        Column {
            Divider()
            BottomNavigation(
                backgroundColor = MaterialTheme.colors.surface,
                contentColor = MaterialTheme.colors.onSurface,
                elevation = 0.dp
            ) {
                Screen.All.bottomNavItems.forEach {
                    BottomNavigationItem(
                        selected = it.route==selectedScreenRoute,
                        onClick = {
                            navController.navigate(it.route){
                                launchSingleTop=true
                                popUpTo(Screen.Home.route)
                            }
                        },
                        alwaysShowLabel = false,
                        icon={

                            if(it.route==Screen.Notifications.route){
                                val unSeenNotifications=vm.notifications.value.filter {notification->
                                    notification.seen == false
                                }

                                if(unSeenNotifications.isEmpty()){
                                    Icon(imageVector =it.icon!!,null )
                                }else{
                                    NotificationsActiveIcon()
                                }

                            }else{
                                if (it.icon!=null)
                                    Icon(imageVector =it.icon ,null )
                                else
                                    Icon(painter = painterResource(it.drawable!!) ,null )
                            }


                        },
                        label = {
                            Text(text = stringResource(it.title),maxLines = 1)
                        },
                    )
                }

            }
        }

    }


    @Composable
    private fun Drawer(){
        MainDrawer(
            currentRoute = vm.currentScreen.value.route,
            onNavigate = {
                toggleDrawer()
                navController.navigate(it)
            },
            onLogOut = {
                logOut()
            }
        )
    }

    private fun logOut(){
        Firebase.auth.signOut()
        startActivity(Intent(this,AuthenticationActivity::class.java))
        finish()
    }


}