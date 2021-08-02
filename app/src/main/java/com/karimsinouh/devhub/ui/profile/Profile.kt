package com.karimsinouh.devhub.ui.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.karimsinouh.devhub.R
import com.karimsinouh.devhub.data.User
import com.karimsinouh.devhub.ui.items.PostItem
import com.karimsinouh.devhub.ui.main.MainViewModel
import com.karimsinouh.devhub.ui.theme.DevhubTheme
import com.karimsinouh.devhub.ui.viewUsersList.VIEW_FOLLOWERS
import com.karimsinouh.devhub.ui.viewUsersList.VIEW_FOLLOWING
import com.karimsinouh.devhub.utils.Screen
import com.karimsinouh.devhub.utils.ScreenState
import com.karimsinouh.devhub.utils.customComposables.CenterProgress
import com.karimsinouh.devhub.utils.customComposables.ChipsList
import com.karimsinouh.devhub.utils.customComposables.OutlinedRoundedButton
import com.karimsinouh.devhub.utils.customComposables.RoundedButton

@Composable
fun Profile(
    nav:NavController,
    vm:MainViewModel
){
    vm.profileState.value.let {
        when(it){
           ScreenState.LOADING-> CenterProgress()

           ScreenState.IDLE->{
               LazyColumn(
                   verticalArrangement = Arrangement.spacedBy(12.dp)
               ) {
                   item {
                       UserInfoSection(nav,vm.user.value!!,vm.userPosts.value.size)
                   }

                   item {
                       SkillsSection(vm.user.value?.skills?: emptyList())
                   }

                   items(vm.userPosts.value){item->
                       PostItem(item) {
                           nav.navigate(Screen.ViewPost.constructRoute(item.id!!))
                       }
                   }

               }
           }
        }
    }
}

@Composable
fun UserInfoSection(
    nav:NavController,
    user: User,
    postCount:Int,
    currentUid:String?=Firebase.auth.currentUser?.uid!!
){

    Column(
        modifier= Modifier
            .background(MaterialTheme.colors.surface)
            .padding(12.dp),
    ) {

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ){

            val painter= rememberImagePainter(user.picture!!)

            Image(
                painter = painter,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(90.dp)
                    .clip(CircleShape)
            )

            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "${user.followers?.size ?: 0}\nFollowers",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.clickable {
                        nav.navigate(Screen.ViewUsersList.constructRoute(user.id!!, VIEW_FOLLOWERS))
                    }
                )


                Text(
                    text = "${user.following?.size ?: 0}\nFollowing",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.clickable {
                        nav.navigate(Screen.ViewUsersList.constructRoute(user.id!!, VIEW_FOLLOWING))
                    }
                )


                Text(text="${postCount}\nPosts",textAlign = TextAlign.Center)
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(text = user.name ?: "User Name",fontSize=20.sp,fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = user.bio?:"",
            color = MaterialTheme.colors.onBackground.copy(alpha = 0.7f),
        )

        //social network
        Spacer(modifier = Modifier.height(4.dp))
        SocialNetwork(
            github = user.github,
            behance = user.behance,
            dribble = user.dribble
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (currentUid!=user.id!!)
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                val followed=user.followers?.contains(currentUid)!!

                RoundedButton(
                    text = stringResource(if (followed) R.string.unFollow else R.string.follow ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    if (followed)
                        user.unFollow(currentUid!!)
                    else
                        user.follow(currentUid!!)
                }

                OutlinedRoundedButton(
                    text = stringResource(R.string.message),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {

                }
            }

    }

}

@Composable
fun SkillsSection(skills:List<String>){
    Column(
        modifier= Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.surface)
            .padding(12.dp),
    ) {
        Text(text="Skills",fontSize = 18.sp,fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        ChipsList(list = skills)
    }

}

@Composable
fun SocialNetwork(
    github:String?="",
    behance:String?="",
    dribble:String?="",
){

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (github!="")
            SocialNetworkItem(
                icon = R.drawable.ic_github,
                text = github!!
            ) {

            }

        if (behance!="")
            SocialNetworkItem(
                icon = R.drawable.ic_behance,
                text = behance!!
            ) {

            }

        if (dribble!="")
            SocialNetworkItem(
                icon = R.drawable.ic_dribble,
                text = dribble!!
            ) {

            }
    }
}

@Composable
fun SocialNetworkItem(
    icon:Int,
    text:String,
    onClick:()->Unit
){
    Row(
        verticalAlignment = Alignment.CenterVertically
    ){
       Icon(
           painter = painterResource(icon),
           contentDescription =null,
           modifier = Modifier.size(18.dp)
       )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "/$text",
            fontSize = 14.sp,
            modifier=Modifier.clickable { onClick() }
        )
    }
}