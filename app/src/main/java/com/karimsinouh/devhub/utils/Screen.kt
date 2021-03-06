package com.karimsinouh.devhub.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.*
import androidx.compose.ui.graphics.vector.ImageVector
import com.karimsinouh.devhub.R

sealed class Screen(
    val route:String,
    val title:Int,
    val icon:ImageVector?=null,
    val drawable:Int?=null,
) {

    object Home: Screen("home", R.string.home, Icons.Outlined.Home)
    object Search: Screen("Search",R.string.search,Icons.Outlined.Search)
    object Profile: Screen("profile", R.string.profile, Icons.Outlined.Person)
    object Notifications: Screen("notifications", R.string.notifications,Icons.Outlined.Notifications)
    object CreateNew:Screen("creaateNew",R.string.createNew)

    object Login:Screen("login",R.string.login)
    object SignUp:Screen("signUp",R.string.signUp)
    object OnBoarding:Screen("onBoarding",R.string.onBoarding)

    object EditProfile:Screen("edit_profile",R.string.editProfile,Icons.Outlined.Edit)

    object ViewPost:Screen("viewPost/{postId}",R.string.viewPost){
        fun constructRoute(postId:String)= route.split("/")[0]+"/"+postId
    }

    object ViewProfile:Screen("viewProfile/{uid}",R.string.visit_profile){
        fun constructRoute(uid:String)=route.split("/")[0]+"/"+uid
    }

    object ViewUsersList:Screen("viewUsersList?uid={uid}&action={action}",R.string.viewUsersList){
        fun constructRoute(uid:String,action:Int)= "viewUsersList?uid=$uid&action=$action"
    }

    object EditPost:Screen("editPost/{postId}",R.string.editPost){
        fun constructRoute(postId:String)=route.split("/")[0]+"/"+postId
    }

    object ViewHashtags:Screen("viewHashtags/{hashtag}",R.string.hashtag){
        fun constructRoute(hashtag:String)=route.split("/")[0]+"/"+hashtag
    }

    object ViewPicture:Screen("viewPicture?url={url}",R.string.picture){
        fun constructRoute(url:String)="viewPicture?url=$url"
    }


    object All{

        fun map():Map<String,Screen>{
            val map=HashMap<String,Screen>()
            allItems.forEach {
                map[it.route]=it
            }
            return map
        }

        val bottomNavItems= listOf(
            Home, Search, Profile, Notifications
        )

        val drawerItems=listOf(
            EditProfile
        )

        private val allItems= listOf(
            Home,
            Search,
            Profile,
            Notifications,
            EditProfile,
            CreateNew,
            ViewPost,
            ViewProfile,
            ViewUsersList,
            EditPost,
            ViewHashtags,
            ViewPicture
        )
    }

}