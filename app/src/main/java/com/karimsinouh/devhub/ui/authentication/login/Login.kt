package com.karimsinouh.devhub.ui.authentication.login

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.MailOutline
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.karimsinouh.devhub.R
import com.karimsinouh.devhub.utils.Screen
import com.karimsinouh.devhub.utils.ScreenState
import com.karimsinouh.devhub.utils.customComposables.CenterProgress
import com.karimsinouh.devhub.utils.customComposables.ErrorDialog
import com.karimsinouh.devhub.utils.customComposables.RoundedButton

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun Login(
    nav:NavController,
    vm:LoginViewModel= viewModel(),
    onLoggedIn:()->Unit
){
    val context= LocalContext.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier= Modifier
            .fillMaxSize()
            .padding(32.dp)
    ){
        Text(
            text= stringResource(R.string.login),
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text= stringResource(R.string.login_text),
            modifier = Modifier.fillMaxWidth(),
            color=MaterialTheme.colors.onBackground.copy(alpha = 0.7f)
        )
        Spacer(modifier = Modifier.height(24.dp))




        //text fields
        TextField(
            value = vm.email.value,
            onValueChange = {
                            vm.email.value=it
            },
            leadingIcon = {Icon(Icons.Outlined.MailOutline,null)},
            placeholder = { Text(text = stringResource(R.string.email))},
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(24.dp))

        TextField(
            value = vm.password.value,
            onValueChange = {
                            vm.password.value=it
            },
            leadingIcon = {Icon(Icons.Outlined.Lock,null)},
            placeholder = { Text(text = stringResource(id = R.string.password))},
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Password,
            ),
            keyboardActions = KeyboardActions(onDone = {
                vm.login(context)
            }),
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(24.dp))

        AnimatedVisibility(
            visible = vm.screenState.value!=ScreenState.LOADING,
        ) {
            RoundedButton(
                text = stringResource(id = R.string.login),
                modifier = Modifier.fillMaxWidth()
            ) {
                vm.login(context)
            }
        }

        AnimatedVisibility(
            visible = vm.screenState.value==ScreenState.LOADING,
        ) {
            CenterProgress(60.dp)
        }

        if (vm.screenState.value==ScreenState.ERROR){
            ErrorDialog(vm.error){
                vm.clearError()
            }
        }

        if (vm.screenState.value==ScreenState.DONE){
            onLoggedIn()
        }

        Spacer(modifier = Modifier.height(24.dp))
        SignUpSection(nav = nav)



    }
}

@Composable
private fun SignUpSection(nav:NavController){
    val signUpText=buildAnnotatedString {
        append(stringResource(R.string.dont_have_account))
        withStyle(style = SpanStyle(color = MaterialTheme.colors.primary)){
            append(stringResource(id = R.string.signUp))
        }
        append(" .")
    }

    Text(
        text = signUpText,
        modifier= Modifier
            .fillMaxWidth()
            .clickable
            {
                nav.navigate(Screen.SignUp.route){
                    launchSingleTop=true
                }
            },
        textAlign = TextAlign.Center
    )
}

