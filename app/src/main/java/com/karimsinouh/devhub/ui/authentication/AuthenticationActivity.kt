package com.karimsinouh.devhub.ui.authentication

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.karimsinouh.devhub.R
import com.karimsinouh.devhub.ui.authentication.login.Login
import com.karimsinouh.devhub.ui.authentication.onBoarding.OnBoarding
import com.karimsinouh.devhub.ui.authentication.signUp.SignUp
import com.karimsinouh.devhub.ui.main.MainActivity
import com.karimsinouh.devhub.ui.theme.DevhubTheme
import com.karimsinouh.devhub.utils.Screen
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AuthenticationActivity: ComponentActivity() {

    private lateinit var nav:NavHostController

    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Devhub)
        setContent {
            nav= rememberAnimatedNavController()

            DevhubTheme {
                window.statusBarColor=MaterialTheme.colors.surface.toArgb()
                Surface(Modifier.fillMaxSize()) {
                    AuthNavHost()
                }
            }
        }

    }

    @OptIn(ExperimentalPagerApi::class)
    @ExperimentalAnimationApi
    @Composable
    private fun AuthNavHost(){
        AnimatedNavHost(navController = nav,startDestination = Screen.OnBoarding.route ){

            composable(route=Screen.Login.route,
                enterTransition = {_,_->
                    slideInHorizontally(initialOffsetX = {1000})
                },
                exitTransition = {_,_->
                    slideOutHorizontally(targetOffsetX = {-1000})
                },
                popEnterTransition = {_,_->
                    slideInHorizontally(initialOffsetX = {-1000})
                },
                popExitTransition = {_,_->
                    slideOutHorizontally(targetOffsetX = {1000})
                }){
                Login(nav = nav){
                    openMainActivity()
                }
            }

            composable(route=Screen.SignUp.route,
                enterTransition = {_,_->
                    slideInHorizontally(initialOffsetX = {1000})
                },
                exitTransition = {_,_->
                    slideOutHorizontally(targetOffsetX = {-1000})
                },
                popEnterTransition = {_,_->
                    slideInHorizontally(initialOffsetX = {-1000})
                },
                popExitTransition = {_,_->
                    slideOutHorizontally(targetOffsetX = {1000})
                }){
                SignUp(nav = nav){
                    openMainActivityForFirstTime()
                }
            }

            composable(route=Screen.OnBoarding.route,
                enterTransition = {_,_->
                    slideInHorizontally(initialOffsetX = {1000})
                },
                exitTransition = {_,_->
                    slideOutHorizontally(targetOffsetX = {-1000})
                },
                popEnterTransition = {_,_->
                    slideInHorizontally(initialOffsetX = {-1000})
                },
                popExitTransition = {_,_->
                    slideOutHorizontally(targetOffsetX = {1000})
                }){
                OnBoarding(nav = nav)
            }


        }
    }

    private fun openMainActivity(){
        val intent= Intent(this,MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun openMainActivityForFirstTime(){
        val intent= Intent(this,MainActivity::class.java)
        intent.apply {
            action="completeProfile"
        }
        startActivity(intent)
        finish()
    }

}