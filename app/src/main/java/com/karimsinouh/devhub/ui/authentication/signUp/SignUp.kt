package com.karimsinouh.devhub.ui.authentication.signUp

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
import androidx.compose.material.icons.outlined.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
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
fun SignUp(
    nav:NavController,
    vm:SignUpViewModel= viewModel(),
    onSignUp:()->Unit
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
            text= stringResource(R.string.signup),
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text= stringResource(R.string.signup_text),
            modifier = Modifier.fillMaxWidth(),
            color= MaterialTheme.colors.onBackground.copy(alpha = 0.7f)
        )
        Spacer(modifier = Modifier.height(24.dp))

        TextField(
            value = vm.name.value,
            onValueChange =
            {
                vm.name.value=it
            },
            leadingIcon = { Icon(Icons.Outlined.Person,null) },
            placeholder = { Text(text = stringResource(R.string.name)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(24.dp))

        TextField(
            value = vm.email.value,
            onValueChange =
            {
                vm.email.value=it
            },
            leadingIcon = { Icon(Icons.Outlined.MailOutline,null) },
            placeholder = { Text(text = stringResource(R.string.email)) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(24.dp))

        val visualTransformation=if(vm.passwordVisibility.value) VisualTransformation.None else PasswordVisualTransformation()
        val icon=if(vm.passwordVisibility.value) R.drawable.design_ic_visibility_off else R.drawable.design_ic_visibility


        TextField(
            value = vm.password.value,
            onValueChange =
            {
                vm.password.value=it
            },
            leadingIcon = { Icon(Icons.Outlined.Lock,null) },
            placeholder = { Text(text = stringResource(R.string.password)) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password,imeAction = ImeAction.Done),
            visualTransformation = visualTransformation,
            keyboardActions = KeyboardActions(onDone = {
                vm.signUp()
            }),
            singleLine = true,
            trailingIcon = {
                IconButton(onClick = { vm.togglePassword() }) {
                    Icon( painterResource(icon),null)
                }
            }
        )
        Spacer(modifier = Modifier.height(24.dp))

        AnimatedVisibility(
            visible = vm.screenState.value!=ScreenState.LOADING,
        ) {
            RoundedButton(
                text = stringResource(id = R.string.signUp),
                modifier = Modifier.fillMaxWidth()
            ) {
                vm.signUp()
            }
        }

        AnimatedVisibility(
            visible = vm.screenState.value==ScreenState.LOADING,
        ) {
            CenterProgress(60.dp)
        }

        if (vm.screenState.value==ScreenState.ERROR){
            ErrorDialog(vm.error){
                vm.error=""
            }
        }

        if (vm.screenState.value==ScreenState.DONE){
            onSignUp()
        }

        Spacer(modifier = Modifier.height(24.dp))


        val signUpText= buildAnnotatedString {
            append(stringResource(R.string.already_have_account))
            withStyle(
                style = SpanStyle(color = MaterialTheme.colors.primary),
            ){
                append(stringResource(id = R.string.login))
            }
            append(" .")
        }

        Text(
            text = signUpText,
            modifier= Modifier
                .fillMaxWidth()
                .clickable
                {
                    nav.navigate(Screen.Login.route) {
                        launchSingleTop = true
                    }
                },
            textAlign = TextAlign.Center
        )

    }
}
