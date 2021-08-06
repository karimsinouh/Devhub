package com.karimsinouh.devhub.ui.authentication

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Devhub)
        setContent {
            nav= rememberNavController()

            DevhubTheme {
                window.statusBarColor=MaterialTheme.colors.surface.toArgb()
                Surface(Modifier.fillMaxSize()) {
                    AuthNavHost()
                }
            }
        }

    }

    @Composable
    private fun AuthNavHost(){
        NavHost(navController = nav,startDestination = Screen.OnBoarding.route ){

            composable(Screen.Login.route){
                Login(nav = nav){
                    openMainActivity()
                }
            }

            composable(Screen.SignUp.route){
                SignUp(nav = nav){
                    openMainActivityForFirstTime()
                }
            }

            composable(Screen.OnBoarding.route){
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