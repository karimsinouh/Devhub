package com.karimsinouh.devhub.ui.createNew

import android.net.Uri
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.karimsinouh.devhub.data.Post
import com.karimsinouh.devhub.utils.ScreenState
import com.karimsinouh.devhub.utils.isValidContent
import com.karimsinouh.devhub.utils.isValidTitle
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CreateNewViewModel @Inject constructor(): ViewModel() {

    val selectedPostType= mutableStateOf(0)
    val title= mutableStateOf("")
    val content= mutableStateOf("")
    val hashtags= mutableStateOf("")
    val pictures= mutableStateListOf<Uri>()
    val state= mutableStateOf(ScreenState.IDLE)
    var error:String =""

    set(value){
        if (value=="")
            state.value=ScreenState.IDLE
        else
            state.value=ScreenState.ERROR
        field=value
    }

    fun getHashtags(value:String):List<String>{
        val list= value.lowercase(Locale.getDefault()).split(",").toMutableList()
        list.remove("")
        list.remove(" ")
        list.remove("  ")
        return list
    }

    fun post(execute:(Post)->Unit){
        if (!isValidTitle(title.value)){
            error="Please enter a valid title, it must be 10 letters long or more."
            return
        }

        if (!isValidContent(content.value)){
            error="Please enter a valid content, it must be 20 letters long or more."
            return
        }

        val post= Post(
            title.value,
            content.value,
            getHashtags(hashtags.value),
            type = selectedPostType.value
        )
        execute(post)

    }

}