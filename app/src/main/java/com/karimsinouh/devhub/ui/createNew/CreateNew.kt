package com.karimsinouh.devhub.ui.createNew

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import com.karimsinouh.devhub.R
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.karimsinouh.devhub.data.Post
import com.karimsinouh.devhub.services.UploadPostService
import com.karimsinouh.devhub.utils.ImagePicker
import com.karimsinouh.devhub.utils.ScreenState
import com.karimsinouh.devhub.utils.customComposables.*
import com.karimsinouh.devhub.utils.toBitmap
import com.theartofdev.edmodo.cropper.CropImage

@Composable
fun CreateNew(
    vm:CreateNewViewModel=viewModel(),
    nav:NavController
) {
    when(vm.state.value){

        ScreenState.IDLE-> Content(vm = vm)

        ScreenState.DONE-> MessageScreen(
            title = "Uploading your post..",
            message = "You can close this screen now, your post will be uploaded in the background",
            button = "Close",
            onClick = { nav.popBackStack() }
        )

        ScreenState.ERROR-> ErrorDialog(error = vm.error) {
            vm.error=""
        }

    }
}



@Composable
private fun Content(
    vm:CreateNewViewModel
){

    val postTypes= stringArrayResource(id = R.array.postTypes).toList()

    val context= LocalContext.current
    val imagePicker=ImagePicker(context)

    val cropLauncher= rememberLauncherForActivityResult(contract = imagePicker.cropContract) {
        if(it.resultCode== Activity.RESULT_OK && it.data!=null) {
            val uri=CropImage.getActivityResult(it.data).uri
            vm.pictures.add(uri)
        }
        else
            vm.error="something went wrong"
    }


    val launcher= rememberLauncherForActivityResult(contract = imagePicker.contract) {
        it?.let{uri->
            imagePicker.openCrop(uri,cropLauncher)
        }
    }



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
            placeholder = {Text("Title")},
            modifier=Modifier.fillMaxWidth(),
            label = { Text(text = "Title") }
        )

        TextField(
            value = vm.content.value,
            onValueChange = {vm.content.value=it},
            placeholder = {Text("content")},
            modifier=Modifier.fillMaxWidth(),
            label = { Text(text = "Content") }
        )

        TextField(
            value = vm.hashtags.value,
            onValueChange = {vm.hashtags.value=it},
            placeholder = {Text("hashtags")},
            modifier=Modifier.fillMaxWidth(),
            label = { Text(text = "Hashtags") }
        )

        vm.hashtags.value.let {
            if (it.isNotEmpty())
                ChipsList(list = vm.getHashtags(it))
        }

        DropDownItems(items = postTypes,vm.selectedPostType.value) {
            vm.selectedPostType.value=it
        }

        PicturesRow(
            pictures = vm.pictures,
            onRemove = { vm.pictures.removeAt(it) },
            onButtonClick = {
                imagePicker.open(launcher)
            }
        )



        RoundedButton(
            text = "Submit",
            modifier=Modifier.fillMaxWidth()
        ) {
            vm.post{
                startService(
                    context,
                    it,
                    vm.pictures
                )
                vm.state.value=ScreenState.DONE
            }
        }

    }

}


private fun startService(
    context:Context,
    post: Post,
    pictures:List<Uri>?
){
    val intent=Intent(context,UploadPostService::class.java)
    intent.putExtra("post",post)
    intent.putParcelableArrayListExtra("bitmaps", ArrayList(pictures))
    context.startService(intent)
}

@Composable
fun PicturesRow(
    pictures:List<Uri>,
    onRemove:(index:Int)->Unit,
    onButtonClick:()->Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ){

        IconButton(
            onClick = onButtonClick,
            modifier= Modifier
                .clip(CircleShape)
                .background(MaterialTheme.colors.onBackground.copy(alpha = 0.1f))
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_photo),
                contentDescription = null,
                tint=MaterialTheme.colors.primary
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
        ){
            pictures.forEachIndexed {i,picture->
                PictureItem(picture = picture) {
                    onRemove(i)
                }
            }
        }

    }
}

@Composable
fun PictureItem(
    picture:Uri,
    onDelete:()->Unit
) {

    val c= LocalContext.current

    Box(
        modifier=Modifier.size(120.dp)
    ){
        Image(
            bitmap = picture.toBitmap(c)?.asImageBitmap()!!,
            contentDescription = null,
            modifier=Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        IconButton(
            onClick = onDelete,
            modifier = Modifier.background(Color.Black)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = null,
                tint=Color.White
            )
        }

    }
}