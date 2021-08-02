package com.karimsinouh.devhub.ui.chat.chatRooms

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChatRoomsViewModel @Inject constructor():ViewModel() {

    val query= mutableStateOf("")

    val chatRooms= mutableStateOf<List<String>>(emptyList())

}