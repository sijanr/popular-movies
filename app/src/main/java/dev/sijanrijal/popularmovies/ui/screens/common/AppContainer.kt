package dev.sijanrijal.popularmovies.ui.screens.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import dev.sijanrijal.popularmovies.navigation.ScreenRouteNavigation

private val bottomBarItems = listOf(
    ScreenRouteNavigation.Home.route() to Icons.Default.Home,
    ScreenRouteNavigation.Search.route() to Icons.Default.Search
)

@ExperimentalAnimationApi
@Composable
fun AppContainer(navController: NavHostController, content: @Composable (innerPadding: PaddingValues) -> Unit) {
    Surface(modifier = Modifier.fillMaxSize()) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        Scaffold(
            bottomBar = {
                PopularMoviesBottomAppbar(navBackStackEntry = navBackStackEntry) {navigationDestination ->
                    navController.navigate(navigationDestination)
                }
            },
            floatingActionButton = {
                FloatingActionButton(onClick = {}, shape = CircleShape) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = null
                    )
                }
            },
            floatingActionButtonPosition = FabPosition.Center,
            isFloatingActionButtonDocked = true
        ) {
            content(it)
        }
    }
}

@ExperimentalAnimationApi
@Composable
fun PopularMoviesBottomAppbar(navBackStackEntry: NavBackStackEntry?, onBottomBarContentClicked: (contentText: String) -> Unit ) {
    BottomAppBar(
        modifier = Modifier
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(
                    topStartPercent = 30,
                    topEndPercent = 30
                )
            ),
        backgroundColor = MaterialTheme.colors.background,
        cutoutShape = CircleShape,
        contentPadding = PaddingValues(horizontal = 4.dp, vertical = 8.dp)
    ) {
        Row(modifier = Modifier
            .background(MaterialTheme.colors.background)
            .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            for ((text, icon) in bottomBarItems) {
                val bottomBarText = when(text) {
                    ScreenRouteNavigation.Home.route() -> "Home"
                    else -> "Search"
                }
                val isSelected = navBackStackEntry?.destination?.route == text
                val animateBottomAppBarContentBackground by animateColorAsState(
                    targetValue = if (isSelected) Color.Blue.copy(
                        alpha = 0.3f
                    ) else MaterialTheme.colors.background
                )
                val animateIconTint by animateColorAsState(
                    targetValue = if (isSelected) MaterialTheme.colors.onBackground else Color.Gray
                )
                BottomAppBarContent(
                    text = bottomBarText,
                    isSelected = isSelected,
                    imageVector = icon,
                    iconTintColor = animateIconTint,
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(color = animateBottomAppBarContentBackground)
                        .padding(end = 28.dp)
                        .animateContentSize()
                ) {
                    if (!isSelected) {
                        onBottomBarContentClicked(text)
                    }
                }
            }
        }
    }
}

@ExperimentalAnimationApi
@Composable
fun BottomAppBarContent(modifier: Modifier = Modifier, text: String, isSelected: Boolean, imageVector: ImageVector, iconTintColor: Color, onIconClick: () -> Unit) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { onIconClick() }) {
            Icon(imageVector = imageVector, contentDescription = null, tint = iconTintColor)
        }
        AnimatedVisibility(visible = isSelected) {
            Text(text = text, color = MaterialTheme.colors.onBackground)
        }
    }
}