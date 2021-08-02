package com.karimsinouh.devhub.utils.customComposables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.karimsinouh.devhub.ui.theme.Shapes

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun DropDownItems(
    items:List<String>,
    selectedIndex:Int=0,
    onItemClick:(index:Int)->Unit
){

    val expanded = remember{
        mutableStateOf(false)
    }

    Column(
        modifier= Modifier
            .clip(Shapes.small)
            .background(MaterialTheme.colors.onBackground.copy(alpha = 0.1f))
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        //top head
        Row(
            modifier=Modifier.fillMaxWidth()
        ){
            Text(
                text=items[selectedIndex],
                color=MaterialTheme.colors.onBackground.copy(alpha = 0.7f),
                modifier = Modifier
                    .padding(16.dp)
                    .weight(0.9f)
            )
            IconButton(onClick = {
                expanded.value= !expanded.value
            }) {

                Icon(
                    if(expanded.value)
                        Icons.Outlined.KeyboardArrowUp
                    else
                        Icons.Outlined.KeyboardArrowDown,
                    null)
            }
        }

        //items
        AnimatedVisibility(expanded.value) {
           Column {
               items.forEachIndexed {i,it->
                   val selected= i==selectedIndex
                   Text(
                       text=it,
                       color=MaterialTheme.colors.onBackground.copy(alpha = if (selected) 01f else 0.7f),
                       modifier = Modifier
                           .fillMaxWidth()
                           .clickable {
                               expanded.value=false
                               onItemClick(i)
                           }
                           .padding(16.dp)
                   )
               }
           }
        }

    }

}