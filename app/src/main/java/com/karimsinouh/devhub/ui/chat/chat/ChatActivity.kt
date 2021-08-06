package com.karimsinouh.devhub.ui.chat.chat

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Send
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.karimsinouh.devhub.data.User
import com.karimsinouh.devhub.R
import com.karimsinouh.devhub.data.Message
import com.karimsinouh.devhub.ui.items.ProfilePicture
import com.karimsinouh.devhub.ui.theme.DevhubTheme
import com.karimsinouh.devhub.ui.theme.Shapes
import com.karimsinouh.devhub.utils.ImagePicker
import com.karimsinouh.devhub.utils.ScreenState
import com.karimsinouh.devhub.utils.customComposables.CenterProgress
import com.karimsinouh.devhub.utils.customComposables.MessageScreen
import com.theartofdev.edmodo.cropper.CropImage
import dagger.hilt.android.AndroidEntryPoint
import io.grpc.InternalChannelz.id


@AndroidEntryPoint
class ChatActivity:ComponentActivity() {

    companion object{
        fun open(
            c: Context,
            user: User?=null,
            chatRoomId: String? = null,
            userId:String?=null,
        ){
            val intent= Intent(c,ChatActivity::class.java)
            intent.putExtra("user",user)
            intent.putExtra("chatRoomId",chatRoomId)
            intent.putExtra("userId",userId)
            c.startActivity(intent)
        }
    }

    private val vm by viewModels<ChatViewModel>()
    private val uid= Firebase.auth.currentUser?.uid!!

    private val imagePicker by lazy {
        ImagePicker(this)
    }

    private lateinit var cropLauncher : ActivityResultLauncher<Intent>

    private lateinit var launcher : ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {


            DevhubTheme {
                window.statusBarColor=MaterialTheme.colors.surface.toArgb()

                Scaffold (
                    topBar = { ChatTopBar() },
                    bottomBar = {MessageInput()},
                    content = {
                        Content()
                    },
                    backgroundColor = MaterialTheme.colors.surface
                )

            }


        }

        val chatRoomId=intent.getStringExtra("chatRoomId")

        intent.getSerializableExtra("user").let { user->

            Log.d("wtf","user: $user")
            if(user!=null){
                vm.user.value=user as User
                vm.loadChatRoom(chatRoomId)
            }else{
                val hisId=intent.getStringExtra("userId")!!
                vm.loadUserFirst(hisId,chatRoomId)
            }

        }

        cropLauncher=registerForActivityResult(imagePicker.cropContract) {
            if (it.resultCode== RESULT_OK && it.data!=null){
                val uri= CropImage.getActivityResult(it.data).uri
                vm.sendPicture(uri)
            }
        }

        launcher=registerForActivityResult(imagePicker.contract) {
            it?.let{uri->
                imagePicker.openCrop(uri,cropLauncher)
            }
        }

    }


    @Composable
    private fun Content(){

        when(vm.state.value){

            ScreenState.IDLE->{
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    reverseLayout = true,
                    modifier=Modifier.padding(bottom = 78.dp),
                    contentPadding = PaddingValues(12.dp),
                ) {

                    itemsIndexed(
                        items=vm.messages.value
                    ){index,item->

                        val isLastMessage=(index)==0

                        if(item.sender==uid)
                            RightMessage(message = item, isLastOne = isLastMessage)
                        else{
                            LeftMessage(message = item)
                            if (isLastMessage && !item.seen!!)
                                item.makeAsSeen(vm.chatRoomId!!)
                        }

                    }

                }
            }

            ScreenState.ERROR->{
                MessageScreen(title = "Hmm..", message = vm.error!!)
            }

            ScreenState.LOADING-> CenterProgress()

        }

    }

    @Composable
    private fun ChatTopBar() {
        TopAppBar(
            backgroundColor = MaterialTheme.colors.surface,
            contentColor = MaterialTheme.colors.onSurface,
            elevation = 0.dp
        ) {

            IconButton(onClick = {finish()}) {
                Icon(Icons.Outlined.ArrowBack,null)
            }

            val user=vm.user.value

            if(user!=null){

                ProfilePicture(
                    url = user.picture!!,
                    size = 40.dp,
                    isOnline = false
                )

                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = user.name?:"Some User",
                        fontSize = 16.sp
                    )
                    if(user.online!!){
                        Text(
                            text = "Online",
                            fontSize = 12.sp,
                            color=MaterialTheme.colors.onSurface.copy(alpha = 0.8f)
                        )
                    }
                }
            }

        }
    }

    @Composable
    @Preview
    private fun MessageInput() {
        TextField(
            value = vm.message.value,
            onValueChange = {vm.message.value=it},
            shape = Shapes.medium,
            colors = TextFieldDefaults.textFieldColors(
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent
            ),
            placeholder = { Text(text = "Message..")},
            trailingIcon = {
                Row {

                    IconButton(onClick = {
                        launcher.launch("image/*")
                    }) {
                        if (vm.pictureState.value==ScreenState.LOADING)
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = MaterialTheme.colors.onSurface,
                                strokeWidth = 2.dp,
                            )
                        else
                            Icon(painter = painterResource(R.drawable.ic_image), contentDescription = null)

                    }

                    IconButton(onClick = { vm.sendTextMessage() }) {
                        Icon(imageVector = Icons.Outlined.Send, contentDescription = null)
                    }

                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        )
    }

    @Composable
    private fun RightMessage(
        message:Message,
        isLastOne:Boolean
    ) {
        Column(
            modifier= Modifier
                .fillMaxWidth()
                .padding(start = 64.dp),
            horizontalAlignment = Alignment.End
        ) {

            if (message.type==Message.TYPE_TEXT)
                Text(
                    text = message.message?:"",
                    color = MaterialTheme.colors.onPrimary,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colors.primary)
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                )
            else {
                val painter= rememberImagePainter(message.message)
                Image(
                    painter = painter,
                    contentDescription = null,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colors.primary)
                        .size(300.dp),
                    contentScale = ContentScale.Crop
                )
            }


            if(isLastOne && message.seen!!)
                Text(
                    text = "Seen",
                    fontStyle = FontStyle.Italic,
                    fontSize = 12.sp
                )

        }
    }

    @Composable
    private fun LeftMessage(
        message:Message
    ) {
        Column(
            modifier= Modifier
                .fillMaxWidth()
                .padding(end = 64.dp),
            horizontalAlignment = Alignment.Start
        ) {

            if (message.type==Message.TYPE_TEXT){
                Text(
                    text = message.message?:"",
                    color = MaterialTheme.colors.onSurface,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colors.onSurface.copy(alpha = 0.1f))
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                )
            }else {
                val painter= rememberImagePainter(message.message)
                Image(
                    painter = painter,
                    contentDescription = null,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colors.onSurface.copy(alpha = 0.1f))
                        .size(300.dp),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }

}