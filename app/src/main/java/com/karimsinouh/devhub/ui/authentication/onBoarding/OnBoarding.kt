package com.karimsinouh.devhub.ui.authentication.onBoarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.karimsinouh.devhub.R
import com.karimsinouh.devhub.utils.Screen
import com.karimsinouh.devhub.utils.customComposables.OutlinedRoundedButton
import com.karimsinouh.devhub.utils.customComposables.RoundedButton

@Composable
fun OnBoarding(nav:NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Image(
            painter = painterResource(id = R.drawable.ic_onboardingimage),
            contentDescription = null
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Welcome to Devshub",
            fontSize = 23.sp,
            fontWeight = FontWeight.Bold,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(id = R.string.onBoardingText),
            modifier = Modifier.width(250.dp),
            textAlign = TextAlign.Justify,
            fontSize=14.sp,
            color=MaterialTheme.colors.onSurface.copy(alpha = 0.8f)
        )

        Spacer(modifier = Modifier.height(12.dp))


        Row(
            modifier = Modifier.width(250.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            RoundedButton(
                text = "Login",
                modifier = Modifier.weight(1f)
            ) {
                nav.navigate(Screen.Login.route)
            }

            OutlinedRoundedButton(
                text = "Sign Up",
                modifier = Modifier.weight(1f)
            ) {
                nav.navigate(Screen.SignUp.route)
            }
        }

    }
}