package com.karimsinouh.devhub.ui.editProfile

import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
import com.karimsinouh.devhub.data.User
import com.karimsinouh.devhub.utils.ScreenState
import com.karimsinouh.devhub.utils.Upload
import com.karimsinouh.devhub.utils.isValidName
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import kotlin.collections.HashMap

@HiltViewModel
class EditProfileViewModel @Inject constructor():ViewModel() {

    var error:String ?=""
        set(value){
            field=value
            if (value!="")
                state.value=ScreenState.ERROR
            else
                state.value=ScreenState.IDLE
        }

    init {
        viewModelScope.launch{
            delay(1000)
            User.get{
                if (it.isSuccessful){
                    val user=it.data!!
                    name.value=user.name?:""
                    bio.value=user.bio?:""
                    picture.value=user.picture?:"https://startupmission.kerala.gov.in/get-image-view/ksum_team/1624432404.jpeg"
                    skills.value=user.skills?.toSkills()?: ""

                    setSocialNetwork("github",user.github!!)
                    setSocialNetwork("behance",user.behance!!)
                    setSocialNetwork("dribble",user.dribble!!)

                    state.value=ScreenState.IDLE
                }else{
                    error=it.message
                }
            }
        }

    }

    val state= mutableStateOf(ScreenState.LOADING)
    val name= mutableStateOf("")
    val bio= mutableStateOf("")
    val picture= mutableStateOf("https://startupmission.kerala.gov.in/get-image-view/ksum_team/1624432404.jpeg")
    val uri= mutableStateOf<Uri?>(null)
    val skills= mutableStateOf("")
    val socialNetwork= mutableStateOf<Map<String,Any>?>(null)

    fun clearError(){
        error=""
    }

    fun update(){

        if (!isValidName(name.value)){
            error="Please enter a valid name"
            return
        }

        val id= Firebase.auth.currentUser?.uid ?: ""

        state.value=ScreenState.LOADING

        val map=HashMap<String,Any>()
        map["name"]=name.value
        map["bio"]=bio.value
        map["skills"]=getSkills()
        map["picture"]=picture.value

        socialNetwork.value.let {
            map["github"]=it?.get("github")?:""
            map["behance"]=it?.get("behance")?:""
            map["dribble"]=it?.get("dribble")?:""
        }

        if (uri.value!=null)
            Upload.image(Upload.PROFILE_PICTURES,uri.value!!,"${id}.jpg"){
                if(it.isSuccessful){
                    map["picture"]=it.data!!
                    updateUser(id,map)
                }else
                    error=it.message

            }
        else
            updateUser(id,map)
    }

    private fun updateUser(id:String,map:Map<String,Any>){

        val profileChanges= userProfileChangeRequest {
            displayName=map["name"].toString()
            photoUri=Uri.parse(map["picture"].toString())
        }

        Firebase.auth.currentUser?.updateProfile(profileChanges)?.addOnCompleteListener {
            if (it.isSuccessful)
                User.update(id,map){updateTask->
                    if(updateTask.isSuccessful)
                        state.value=ScreenState.DONE
                    else
                        error=updateTask.message
                }
            else
                error=it.exception?.message
        }

    }

    private fun List<String>.toSkills():String{
        val str=StringBuilder()
        forEachIndexed {i,it->
            str.append(it + if ((i+1)!=size) "," else "")
        }
        return str.toString()
    }

    fun getSkills():List<String>{
        val list= skills.value.lowercase(Locale.getDefault()).split(",").toMutableList()
        list.remove("")
        list.remove(" ")
        list.remove("  ")
        return list
    }

    fun setSocialNetwork(key:String,value:String){
        val map=socialNetwork.value?.toMutableMap() ?: HashMap()
        map[key]=value
        socialNetwork.value=map
    }


}