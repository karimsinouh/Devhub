package com.karimsinouh.devhub.ui.editProfile

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.karimsinouh.devhub.R
import com.karimsinouh.devhub.utils.ImagePicker
import com.karimsinouh.devhub.utils.ScreenState
import com.karimsinouh.devhub.utils.customComposables.CenterProgress
import com.karimsinouh.devhub.utils.customComposables.ChipsList
import com.karimsinouh.devhub.utils.customComposables.ErrorDialog
import com.karimsinouh.devhub.utils.customComposables.RoundedButton
import com.karimsinouh.devhub.utils.toBitmap
import com.theartofdev.edmodo.cropper.CropImage

@Composable
fun EditProfile(
    nav:NavController,
    vm:EditProfileViewModel= viewModel()
) {

    val context= LocalContext.current

    val imagePicker=ImagePicker(context)


    val cropLauncher= rememberLauncherForActivityResult(imagePicker.cropContract) {
        if (it.resultCode== Activity.RESULT_OK && it.data!=null){
            val uri=CropImage.getActivityResult(it.data).uri
            vm.uri.value=uri
        }
    }

    val launcher= rememberLauncherForActivityResult(imagePicker.contract) {
        it?.let{uri->
            imagePicker.openCrop(uri,cropLauncher)
        }
    }

    when(vm.state.value){
        ScreenState.LOADING-> CenterProgress()

        ScreenState.IDLE->{
            Column(
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier= Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(32.dp)
            ){

                val painter= rememberImagePainter(
                    data = vm.picture.value
                )

                vm.uri.value.let {
                    if (it!=null)
                        Image(
                            contentDescription = null,
                            modifier = Modifier
                                .size(150.dp)
                                .clip(CircleShape)
                                .align(Alignment.CenterHorizontally)
                                .clickable {
                                    imagePicker.open(launcher)
                                },
                            bitmap = it.toBitmap(context)?.asImageBitmap()!!,
                            contentScale = ContentScale.Crop
                        )
                    else
                        Image(
                            contentDescription = null,
                            modifier = Modifier
                                .size(150.dp)
                                .clip(CircleShape)
                                .align(Alignment.CenterHorizontally)
                                .clickable {
                                    imagePicker.open(launcher)
                                },
                            painter=painter,
                            contentScale = ContentScale.Crop
                        )
                }

                TextField(
                    value = vm.name.value,
                    onValueChange = {
                        vm.name.value=it
                    },
                    leadingIcon = { Icon(Icons.Default.Person,null) },
                    placeholder = { Text(stringResource(id = R.string.name)) },
                    singleLine = true,
                    modifier=Modifier.fillMaxWidth(),
                    label = { Text(stringResource(id = R.string.name)) }
                )

                TextField(
                    value = vm.bio.value,
                    onValueChange = {
                        vm.bio.value=it
                    },
                    placeholder = { Text(text = stringResource(id = R.string.bio)) },
                    modifier=Modifier.fillMaxWidth(),
                    maxLines = 10,
                    label = {Text(stringResource(R.string.bio))}
                )


                TextField(
                    value = vm.skills.value,
                    onValueChange = { vm.skills.value=it },
                    placeholder = { Text(text = stringResource(id = R.string.skills)) },
                    modifier=Modifier.fillMaxWidth(),
                    label= { Text(text = stringResource(id = R.string.skills))}
                )
                Text(
                    text = "Separate them by commas , without spacing",
                    fontSize = 11.sp,
                    modifier=Modifier.fillMaxWidth()
                )
                ChipsList(list = vm.getSkills())

                Text(
                    text="Social Networks",
                    modifier=Modifier.fillMaxWidth()
                    )

                TextField(
                    value = vm.socialNetwork.value?.get("github").toString(),
                    onValueChange = {
                        vm.setSocialNetwork("github",it)
                    },
                    placeholder = { Text(text = stringResource(id = R.string.github)) },
                    modifier=Modifier.fillMaxWidth(),
                    maxLines = 10,
                    label = { Text(stringResource(R.string.github)) },
                    leadingIcon = {Icon(painterResource(R.drawable.ic_github),null)}
                )

                TextField(
                    value = vm.socialNetwork.value?.get("behance").toString(),
                    onValueChange = {
                        vm.setSocialNetwork("behance",it)
                    },
                    placeholder = { Text(text = stringResource(id = R.string.behance)) },
                    modifier=Modifier.fillMaxWidth(),
                    maxLines = 10,
                    label = { Text(stringResource(R.string.behance)) },
                    leadingIcon = {Icon(painterResource(R.drawable.ic_behance),null)}
                )


                TextField(
                    value = vm.socialNetwork.value?.get("dribble").toString(),
                    onValueChange = {
                        vm.setSocialNetwork("dribble",it)
                    },
                    placeholder = { Text(text = stringResource(id = R.string.dribble)) },
                    modifier=Modifier.fillMaxWidth(),
                    maxLines = 10,
                    label = { Text(stringResource(R.string.dribble)) },
                    leadingIcon = {Icon(painterResource(R.drawable.ic_dribble),null)}
                )

                RoundedButton(text = stringResource(R.string.done)) {
                    vm.update()
                }

            }
        }

        ScreenState.ERROR->{
            ErrorDialog(vm.error?:"") {
                vm.clearError()
            }
        }

        ScreenState.DONE->{
            nav.popBackStack()
        }
    }
}
