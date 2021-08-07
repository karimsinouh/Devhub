package com.karimsinouh.devhub.ui.authentication.login

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.karimsinouh.devhub.ui.main.MainActivity
import com.karimsinouh.devhub.utils.ScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(): ViewModel() {

    val email= mutableStateOf("")
    val password= mutableStateOf("")
    val screenState= mutableStateOf(ScreenState.IDLE)

    val passwordVisibility= mutableStateOf(false)

    var error:String=""
    set(value){
        screenState.value = if (value!="")
            ScreenState.ERROR
        else
            ScreenState.IDLE
        field=value
    }

    fun login(c: Context)=viewModelScope.launch{
        if (email.value=="" || password.value==""){
            screenState.value=ScreenState.ERROR
            error="Please fill in all the fields to continue"
            return@launch
        }
        screenState.value=ScreenState.LOADING

        Firebase.auth.signInWithEmailAndPassword(email.value,password.value).addOnCompleteListener {
            if(it.isSuccessful)
                screenState.value=ScreenState.DONE
            else
                error=it.exception?.message?:""

        }



    }

    fun clearError() {
        error=""
    }

    fun togglePassword() {
        passwordVisibility.value= !passwordVisibility.value
    }

}