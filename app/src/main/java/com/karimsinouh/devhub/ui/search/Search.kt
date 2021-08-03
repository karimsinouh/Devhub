package com.karimsinouh.devhub.ui.search

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.karimsinouh.devhub.R
import com.karimsinouh.devhub.ui.items.PostItem
import com.karimsinouh.devhub.ui.items.UserItem
import com.karimsinouh.devhub.utils.Screen
import com.karimsinouh.devhub.utils.ScreenState
import com.karimsinouh.devhub.utils.customComposables.CenterProgress
import com.karimsinouh.devhub.utils.customComposables.MessageScreen

@ExperimentalFoundationApi
@Composable
fun Search(
    nav:NavController,
    vm:SearchViewModel= viewModel()
){

    Column(
        modifier=Modifier.fillMaxSize()
    ) {

        SearchTextField(vm)

        when(vm.state.value){
            ScreenState.IDLE->
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ){

                    val isSearchingForUser=vm.searchType.value==SearchRepository.TYPE_USER || vm.searchType.value==SearchRepository.TYPE_USER_BY_SKILL

                    if (isSearchingForUser)
                        items(vm.users.value){item->
                            UserItem(user = item) {
                                nav.navigate(Screen.ViewProfile.constructRoute(item.id!!))
                            }
                            Divider()
                        }
                    else
                        items(vm.posts.value){item->
                            PostItem(post = item) {
                                nav.navigate(Screen.ViewPost.constructRoute(item.id!!))
                            }
                        }

                }
            ScreenState.LOADING -> CenterProgress()
            ScreenState.ERROR -> MessageScreen(title = stringResource(R.string.ops), message = vm.error)
            ScreenState.DONE -> Unit
        }
    }


    if (vm.searchTypeVisible.value)
        AlertDialog(
            onDismissRequest = { vm.searchTypeVisible.value=false },
            title = { Text(text = stringResource(R.string.search_for))},
            confirmButton = { TextButton(onClick = { vm.searchTypeVisible.value=false }) {
                Text(text = stringResource(R.string.ok))
            }},
            text = {
                SearchTypeDialog(vm.searchType.value){
                    vm.searchType.value=it
                }
            }
        )

}


@Composable
private fun SearchTextField(
    vm:SearchViewModel
){
    TextField(
        value = vm.query.value,
        onValueChange = {vm.query.value=it},
        placeholder = { Text(text = stringResource(id = R.string.search))},
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth(),
        trailingIcon = {
            Row{

                IconButton(onClick = { vm.searchTypeVisible.value=true }) {
                    Icon(imageVector = Icons.Outlined.Settings, contentDescription = null)
                }
                Spacer(modifier = Modifier.width(4.dp))
                IconButton(onClick = { vm.search() }) {
                    Icon(imageVector = Icons.Outlined.Search, contentDescription = null)
                }
            }
        },
        colors=TextFieldDefaults.textFieldColors(
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent
        ),
        singleLine = true
    )
}

@Composable
private fun SearchTypeDialog(
    selectedItem:Int,
    onItemClick:(index:Int)->Unit
){
    val items=stringArrayResource(id = R.array.postTypes).toMutableList()
    items.add(stringResource(id = R.string.hashtags))
    items.add(stringResource(id = R.string.users))
    items.add(stringResource(id = R.string.usersBySkill))

    Column {
        items.forEachIndexed {i,it->
            Text(
                text = it,
                modifier= Modifier
                    .fillMaxWidth()
                    .clickable { onItemClick(i) }
                    .padding(0.dp, 12.dp),
                color=if(selectedItem==i) MaterialTheme.colors.primary else MaterialTheme.colors.onSurface
            )
            Divider()
        }
    }

}