package com.karimsinouh.devhub.ui.editPost

import android.content.ContentProvider
import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.karimsinouh.devhub.utils.ScreenState
import com.karimsinouh.devhub.utils.customComposables.*

@Composable
fun EditPost(
    postId:String,
    nav:NavController,
    vm:EditPostViewModel= viewModel()
) {

    LaunchedEffect(key1 = postId){
        vm.loadPost(postId)
    }

    when(vm.state.value){

        ScreenState.IDLE-> Content(vm)

        ScreenState.LOADING-> CenterProgress()

        ScreenState.ERROR-> MessageScreen(
            title = "Error!",
            message = vm.error!!,
            button = "Ok",
            onClick = {vm.state.value=ScreenState.IDLE}
        )
        ScreenState.DONE -> MessageScreen(
            title = "Successfully Updated!",
            message = "Your post has been successfully updated. you leave this screen now",
            button = "Finish",
            onClick = {nav.popBackStack()}
        )
    }

}

@Composable
fun Content(vm:EditPostViewModel) {
    Column(
        modifier= Modifier
            .padding(12.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        TextField(
            value = vm.title.value,
            onValueChange = {vm.title.value=it},
            placeholder = { Text("Title") },
            modifier= Modifier.fillMaxWidth(),
            label = { Text(text = "Title") }
        )

        TextField(
            value = vm.content.value,
            onValueChange = {vm.content.value=it},
            placeholder = { Text("content") },
            modifier= Modifier.fillMaxWidth(),
            label = { Text(text = "Content") }
        )

        TextField(
            value = vm.hashtags.value,
            onValueChange = {vm.hashtags.value=it},
            placeholder = { Text("hashtags") },
            modifier= Modifier.fillMaxWidth(),
            label = { Text(text = "Hashtags") }
        )

        vm.hashtags.value.let {
            if (it.isNotEmpty())
                ChipsList(list = vm.getHashtags(it))
        }



        RoundedButton(
            text = "Update",
            modifier= Modifier.fillMaxWidth()
        ) {
            vm.update()
        }

    }
}