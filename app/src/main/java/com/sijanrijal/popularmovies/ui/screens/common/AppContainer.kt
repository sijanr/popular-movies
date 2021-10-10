package com.sijanrijal.popularmovies.ui.screens.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


private val bottomBarItems = listOf(
    "Home" to Icons.Default.Home,
    "Search" to Icons.Default.Search
)

@ExperimentalAnimationApi
@Composable
fun PopularMoviesTheme(content: @Composable (innerPadding: PaddingValues) -> Unit) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            bottomBar = {
                PopularMoviesBottomAppbar()
            }
        ) {
            content(it)
        }
    }
}

@ExperimentalAnimationApi
@Composable
fun PopularMoviesBottomAppbar() {
    BottomAppBar(
        modifier = Modifier
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(
                    topStartPercent = 20,
                    topEndPercent = 20
                )
            ),
        backgroundColor = MaterialTheme.colors.background
    ) {
        var currentSelection by remember {
            mutableStateOf(-1)
        }
        Row(modifier = Modifier
            .background(MaterialTheme.colors.background)
            .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            for ((index, pair) in bottomBarItems.withIndex()) {
                val (text, icon) = pair
                val isIconClicked = currentSelection==index
                val animateBottomAppBarContentBackground by animateColorAsState(
                    targetValue = if (isIconClicked) Color.Blue.copy(
                        alpha = 0.3f
                    ) else MaterialTheme.colors.background
                )
                val animateIconTint by animateColorAsState(
                    targetValue = if (isIconClicked) MaterialTheme.colors.onBackground else Color.Gray
                )
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(color = animateBottomAppBarContentBackground)
                        .padding(end = 28.dp)
                        .animateContentSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    IconButton(onClick = { currentSelection = index }) {
                        Icon(imageVector = icon, contentDescription = null, tint = animateIconTint)
                    }
                    AnimatedVisibility(visible = isIconClicked) {
                        Text(text = text, color = MaterialTheme.colors.onBackground)
                    }
                }
            }
        }
    }
}