package dev.sijanrijal.popularmovies.home.ui

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
import dev.sijanrijal.popularmovies.home.network.UrlProvider
import dev.sijanrijal.popularmovies.home.ui.screens.HomeScreen
import dev.sijanrijal.popularmovies.home.viewmodel.MainViewModel

internal enum class Screen(val route: String) {
    Home("home"),
    Search("search")
}

@Composable
internal fun AppNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    urlProvider: UrlProvider
) {
    NavHost(modifier = modifier, navController = navController, startDestination = Screen.Home.route) {
        addHome(urlProvider)
        addSearch()
    }
}

@ExperimentalCoilApi
private fun NavGraphBuilder.addHome(urlProvider: UrlProvider) {
    composable(Screen.Home.route) {
        val viewModel = hiltViewModel<MainViewModel>()
        HomeScreen(viewModel = viewModel, urlProvider = urlProvider)
    }
}

private fun NavGraphBuilder.addSearch() {
    composable(Screen.Search.route) {
        Text(text = "Search screen", color = LocalContentColor.current, style = MaterialTheme.typography.h3)
    }
}