package com.karimsinouh.devhub.ui.home

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.karimsinouh.devhub.R
import com.karimsinouh.devhub.data.Post
import com.karimsinouh.devhub.ui.items.PostItem
import com.karimsinouh.devhub.ui.main.MainViewModel
import com.karimsinouh.devhub.utils.Screen
import com.karimsinouh.devhub.utils.ScreenState
import com.karimsinouh.devhub.utils.customComposables.CenterProgress
import kotlinx.coroutines.launch

@Composable
fun Home(
    nav:NavController,
    vm:MainViewModel
){

    when(vm.homeState.value){
        ScreenState.IDLE-> Content(nav,vm)
        ScreenState.LOADING-> CenterProgress()
    }

}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun Content(nav:NavController, vm:MainViewModel) {

    val pages= stringArrayResource(id = R.array.postTypes)
    val scope= rememberCoroutineScope()

    Column {
        PostsTabs(
            selectedIndex = vm.pagerState.currentPage,
            onSelect = {
                scope.launch {
                    vm.pagerState.animateScrollToPage(it)
                } },
            pages = pages.toList()
        )

        HorizontalPager(state = vm.pagerState) {page->
            val posts=vm.posts.value.filter { it.type==page }

            PostsList(
                items = posts,
                onHashtagClick = { h->
                    nav.navigate(Screen.ViewHashtags.constructRoute(h))
                }
            ) {
                nav.navigate(Screen.ViewPost.constructRoute(it.id!!))
            }
        }
    }


}

@Composable
fun PostsTabs(
    selectedIndex:Int,
    onSelect:(index:Int)->Unit,
    pages:List<String>,
    edgePadding: Dp=TabRowDefaults.ScrollableTabRowPadding
) {

    ScrollableTabRow(
        selectedTabIndex = selectedIndex,
        backgroundColor = MaterialTheme.colors.surface,
        contentColor = MaterialTheme.colors.onSurface,
        indicator = {tabPositions->
            TabRowDefaults.Indicator(
                color = MaterialTheme.colors.primary,
                modifier = Modifier.tabIndicatorOffset(tabPositions[selectedIndex])
            )
        },
        edgePadding = edgePadding
    ) {

        pages.forEachIndexed { index, s ->

            val isSelected=index==selectedIndex

            Tab(
                selected = isSelected,
                onClick = {onSelect(index)},
                selectedContentColor = MaterialTheme.colors.primary,
                unselectedContentColor = MaterialTheme.colors.onSurface
            ) {
                Text(
                    text = s+"s",
                    modifier = Modifier.padding(16.dp),
                    fontWeight = if(isSelected) FontWeight.Bold else FontWeight.Normal
                )
            }
        }
        
    }
}

@Composable
fun PostsList(
    items:List<Post>,
    onHashtagClick:(String)->Unit,
    onClick:(Post)->Unit
){
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(top = 12.dp,bottom = 69.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(items=items,key={it.id!!}){item->
            PostItem(
                post = item,
                onHashtagClick = onHashtagClick
            ){
                onClick(item)
            }
        }
    }
}

