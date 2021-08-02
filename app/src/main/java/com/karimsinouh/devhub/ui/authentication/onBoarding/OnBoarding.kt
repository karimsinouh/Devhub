package com.karimsinouh.devhub.ui.authentication.onBoarding

import androidx.compose.material.Button
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.karimsinouh.devhub.utils.Screen

@Composable
fun OnBoarding(nav:NavController){
    Button(onClick = {
        nav.navigate(Screen.Login.route)
    }) {
        Text("Skip this")
    }
}

