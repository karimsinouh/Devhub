package com.karimsinouh.devhub.ui.authentication.signUp

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.karimsinouh.devhub.data.User
import com.karimsinouh.devhub.ui.main.MainActivity
import com.karimsinouh.devhub.utils.ScreenState
import com.karimsinouh.devhub.utils.isValidEmail
import com.karimsinouh.devhub.utils.isValidName
import com.karimsinouh.devhub.utils.isValidPassword
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor():ViewModel(){

    val email= mutableStateOf("")
    val password= mutableStateOf("")
    val name= mutableStateOf("")
    val screenState= mutableStateOf(ScreenState.IDLE)
    var error:String=""
        set(value){
            screenState.value=if (value=="")
                ScreenState.IDLE
            else
                ScreenState.ERROR
            field=value
        }

    fun signUp(c:Context)=viewModelScope.launch{
        val _email=email.value
        val _password=password.value
        val _name=name.value

        if(!isValidName(_name)) {
            error = "Please enter a valid name"
            return@launch
        }

        if(!isValidEmail(_email)) {
            error = "Please enter a valid email"
            return@launch
        }

        if(!isValidPassword(_password)) {
            error = "your password must be 6 letters or more long"
            return@launch
        }

        screenState.value=ScreenState.LOADING

        Firebase.auth.createUserWithEmailAndPassword(_email,_password).addOnCompleteListener {
            if (it.isSuccessful){
                val user= User(it.result?.user?.uid, _name, _email)

                user.create { docTask->
                    if(docTask.isSuccessful)
                        screenState.value=ScreenState.DONE
                    else
                        error=docTask.exception?.message?:""

                }
            }
            else
                error=it.exception?.message?:""

        }



    }


}