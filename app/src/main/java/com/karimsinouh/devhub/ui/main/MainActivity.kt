package com.karimsinouh.devhub.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.karimsinouh.devhub.ui.authentication.AuthenticationActivity
import com.karimsinouh.devhub.ui.theme.DevhubTheme
import com.karimsinouh.devhub.ui.theme.DrawerShape
import com.karimsinouh.devhub.utils.Screen
import kotlinx.coroutines.*

class MainActivity : ComponentActivity() {

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

    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            navController= rememberNavController()
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
                    drawerBackgroundColor = MaterialTheme.colors.surface
                ) {


                    MainNavHost(controller = navController, vm = vm)

                    LaunchedEffect(true){
                        delay(100)
                        intent.action?.let { action->
                            if (action=="completeProfile" && !vm.editProfileHasBeenOpened){
                                vm.editProfileHasBeenOpened=true
                                navController.navigate(Screen.EditProfile.route)
                            }
                        }
                    }

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
        MainBottomBar(
            selectedScreenRoute = vm.currentScreen.value.route,
            onNavigation = {
                navController.navigate(it.route){
                    launchSingleTop=true
                    popUpTo(Screen.Home.route)
                }
            }
        )
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