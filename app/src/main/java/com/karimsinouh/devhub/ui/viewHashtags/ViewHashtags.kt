package com.karimsinouh.devhub.ui.viewHashtags

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.karimsinouh.devhub.R
import com.karimsinouh.devhub.ui.home.PostsList
import com.karimsinouh.devhub.ui.home.PostsTabs
import com.karimsinouh.devhub.ui.viewUsersList.UsersList
import com.karimsinouh.devhub.utils.Screen
import com.karimsinouh.devhub.utils.ScreenState
import com.karimsinouh.devhub.utils.customComposables.CenterProgress
import com.karimsinouh.devhub.utils.customComposables.MessageScreen
import kotlinx.coroutines.launch

@Composable
fun ViewHashtags(
    nav:NavController,
    _hashtag:String,
    vm:ViewHashtagsViewModel= viewModel()
){
    vm.hashtag=_hashtag

    LaunchedEffect(vm.hashtag){
        vm.loadPosts(vm.hashtag)
    }

    when(vm.state.value){

        ScreenState.IDLE-> Content(nav,vm)

        ScreenState.ERROR-> MessageScreen(title = "Oops!", message = vm.error!!)

        ScreenState.LOADING-> CenterProgress()

    }

}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun Content(
    nav:NavController,
    vm:ViewHashtagsViewModel
){

    val postTypes= stringArrayResource(id = R.array.postTypes).toMutableList()
    postTypes.add("User")

    val scope= rememberCoroutineScope()

    Column {
        Column(modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.surface)
            .padding(16.dp)
        ) {
            Text(
                text = vm.hashtag,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "All about this hashtag",
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.8f),
                fontSize = 12.sp,
            )
        }
        PostsTabs(
            selectedIndex = vm.pagerState.currentPage,
            onSelect = {
                scope.launch {
                    vm.pagerState.animateScrollToPage(it)
                }
            },
            pages = postTypes,
            edgePadding = 16.dp
        )

        HorizontalPager(state = vm.pagerState) {page->

            if(page!=3){
                val posts=vm.posts.value.filter { it.type==page }

                if(posts.isEmpty())
                    MessageScreen(
                        title = postTypes[page],
                        message = "Sorry, we couldn't find any post that contains this hashtag"
                    )

                PostsList(items = posts,onHashtagClick = {}){
                    nav.navigate(Screen.ViewPost.constructRoute(it.id!!))
                }
            }else{
                val users=vm.users.value

                if(users.isEmpty()){
                    MessageScreen(
                        title = "Users",
                        message = "We couldn't find any users that have this skill or hashtag"
                    )
                }else{
                    UsersList(list = users){
                        nav.navigate(Screen.ViewProfile.constructRoute(it.id!!))
                    }
                }

            }
        }

    }

}