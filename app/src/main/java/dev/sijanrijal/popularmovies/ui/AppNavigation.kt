package dev.sijanrijal.popularmovies.ui

import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import coil.annotation.ExperimentalCoilApi
import dev.sijanrijal.popularmovies.home.ui.HomeScreen
import dev.sijanrijal.popularmovies.home.viewmodel.HomeViewModel
import dev.sijanrijal.popularmovies.navigation.ScreenRouteNavigation

@Composable
internal fun AppNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {
    NavHost(modifier = modifier, navController = navController, startDestination = ScreenRouteNavigation.Home.route()) {
        addHome()
        addSearch()
    }
}

@ExperimentalCoilApi
private fun NavGraphBuilder.addHome() {
    composable(ScreenRouteNavigation.Home.route()) {
        val viewModel = hiltViewModel<HomeViewModel>()
        HomeScreen(viewModel = viewModel)
    }
}

private fun NavGraphBuilder.addSearch() {
    composable(ScreenRouteNavigation.Search.route()) {
        Text(text = "Search screen", color = LocalContentColor.current, style = MaterialTheme.typography.h3)
    }
}