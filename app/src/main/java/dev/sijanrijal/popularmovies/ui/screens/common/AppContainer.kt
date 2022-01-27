package dev.sijanrijal.popularmovies.ui.screens.common

import androidx.annotation.FloatRange
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.*
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import dev.sijanrijal.popularmovies.navigation.MainNavigationScreens
import kotlin.math.min

//Items in the bottom bar
private val bottomBarItems = listOf(
    MainNavigationScreens.Home.route() to Icons.Default.Home,
    MainNavigationScreens.Search.route() to Icons.Default.Search
)

@ExperimentalAnimationApi
@Composable
fun AppContainer(
    navController: NavHostController,
    content: @Composable (innerPadding: PaddingValues) -> Unit
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val showBottomBar = navBackStackEntry?.destination?.route in bottomBarItems.map { it.first }
        Scaffold(
            bottomBar = {
                if (showBottomBar) {
                    PopularMoviesBottomAppbar(navBackStackEntry = navBackStackEntry) { navigationDestination ->
                        navController.navigate(navigationDestination)
                    }
                }
            }
        ) {
            content(it)
        }
    }
}

@ExperimentalAnimationApi
@Composable
fun PopularMoviesBottomAppbar(
    navBackStackEntry: NavBackStackEntry?,
    onBottomBarContentClicked: (contentText: String) -> Unit
) {
    //get the current item in the bottom nav bar and its index
    val currentSelectedItem = bottomBarItems.first { pair -> pair.first == navBackStackEntry?.destination?.route }
    val selectedIndex = bottomBarItems.indexOf(currentSelectedItem)

    PopularMoviesBottomNavBar(
        modifier = Modifier
            .clip(
                RoundedCornerShape(
                    topStartPercent = 30,
                    topEndPercent = 30
                )
            )
            .background(color = MaterialTheme.colors.background)
            .height(50.dp)
            .fillMaxWidth(),
        currentSelectedIndex = selectedIndex,
        indicator = {
            Box(
                modifier = Modifier
                    .background(Color.Transparent)
                    .fillMaxSize()
                    .border(width = 2.dp, color = MaterialTheme.colors.secondaryVariant, RoundedCornerShape(50))
            )
        }
    ) {
        for ((text, icon) in bottomBarItems) {
            val bottomBarText = when (text) {
                MainNavigationScreens.Home.route() -> "Home"
                else -> "Search"
            }
            val isSelected = currentSelectedItem.first == text
            val animateIconTint by animateColorAsState(
                targetValue = if (isSelected) MaterialTheme.colors.onBackground else Color.Gray
            )
            PopularMoviesBottomNavItem(
                selected = isSelected,
                onSelected = { if (!isSelected) onBottomBarContentClicked(text) },
                animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
                text = {
                    Text(text = bottomBarText, color = MaterialTheme.colors.onBackground)
                },
                icon = {
                    Icon(imageVector = icon, contentDescription = null, tint = animateIconTint)
                })
        }
    }
}

@Composable
fun PopularMoviesBottomNavBar(
    modifier: Modifier,
    currentSelectedIndex: Int,
    indicator: @Composable BoxScope.() -> Unit,
    content: @Composable () -> Unit
) {
    // create a spring animation when navigating between different items in bottom nav bar
    val indicatorIndex = remember {
        Animatable(0f)
    }
    val indicatorTargetIndex = currentSelectedIndex.toFloat()
    LaunchedEffect(indicatorTargetIndex) {
        indicatorIndex.animateTo(
            indicatorTargetIndex,
            spring(dampingRatio = Spring.DampingRatioMediumBouncy)
        )
    }

    Layout(modifier = modifier, content = {
        content()
        Box(modifier = Modifier.layoutId("indicator"), content = indicator)
    }) { measurables: List<Measurable>, constraints: Constraints ->
        val itemWidth = (constraints.maxWidth / (bottomBarItems.size))
        val indicatorMeasurable = measurables.first { it.layoutId == "indicator" }
        //measure only the contents except the indicator
        val placeables = measurables.filterNot { it == indicatorMeasurable }.map { measurable ->
                measurable.measure(
                    constraints.copy(
                        minWidth = itemWidth,
                        maxWidth = itemWidth
                    )
                )
            }
        //measure the indicator
        val indicatorPlaceable = indicatorMeasurable.measure(
            constraints.copy(
                minWidth =  itemWidth,
                maxWidth = itemWidth
            )
        )
        //get the layout's width and height
        val width = constraints.maxWidth
        val height = placeables.maxByOrNull { it.height }?.height ?: 0
        layout(width, height) {
            //get indicator's placement information
            val indicatorLeft = indicatorIndex.value * itemWidth
            indicatorPlaceable.placeRelative(x = indicatorLeft.toInt(), y = 0)
            var x = 0
            placeables.forEach { placeable ->
                placeable.placeRelative(x = x, y = 0)
                x += placeable.width
            }
        }
    }
}

@Composable
fun PopularMoviesBottomNavItem(
    modifier: Modifier = Modifier,
    selected: Boolean,
    onSelected: () -> Unit,
    animationSpec: AnimationSpec<Float>,
    text: @Composable BoxScope.() -> Unit,
    icon: @Composable BoxScope.() -> Unit,
) {
    Box(
        modifier = modifier.selectable(selected = selected, onClick = onSelected),
        contentAlignment = Alignment.Center
    ) {
        val animationProgress by animateFloatAsState(
            targetValue = if (selected) 1f else 0f,
            animationSpec = animationSpec
        )
        PopularMoviesNavItemLayout(text = text, icon = icon, animationProgress = animationProgress)
    }
}

@Composable
fun PopularMoviesNavItemLayout(
    text: @Composable BoxScope.() -> Unit,
    icon: @Composable BoxScope.() -> Unit,
    @FloatRange(from = 0.0, to = 1.0) animationProgress: Float
) {
    Layout(content = {
        Box(
            modifier = Modifier
                .layoutId("icon")
                .padding(horizontal = 4.dp), content = icon
        )
        Box(
            modifier = Modifier
                .layoutId("text")
                .padding(horizontal = 4.dp)
                .graphicsLayer {
                    alpha = animationProgress
                    scaleX = min(animationProgress + 0.6f, 1.0f)
                    scaleY = min(animationProgress + 0.6f, 1.0f)
                }, content = text
        )
    }) { measureables, constraints ->
        val iconPlaceable = measureables.first { it.layoutId == "icon" }.measure(constraints)
        val textPlaceable = measureables.first { it.layoutId == "text" }.measure(constraints)
        placeTextAndIcon(
            iconPlaceable,
            textPlaceable,
            constraints.maxWidth,
            constraints.maxHeight,
            animationProgress
        )
    }
}

fun MeasureScope.placeTextAndIcon(
    iconPlaceable: Placeable,
    textPlaceable: Placeable,
    width: Int,
    height: Int,
    @FloatRange(from = 0.0, to = 1.0) animationProgress: Float
): MeasureResult {
    val iconY = (height - iconPlaceable.height) / 2
    val textY = (height - textPlaceable.height) / 2
    val textWidth = textPlaceable.width * animationProgress
    val iconX = (width - textWidth - iconPlaceable.width) / 2
    val textX = iconX + iconPlaceable.width
    return layout(width, height) {
        iconPlaceable.placeRelative(x = iconX.toInt(), y = iconY)
        if (animationProgress != 0f) {
            textPlaceable.placeRelative(x = textX.toInt(), y = textY)
        }
    }
}