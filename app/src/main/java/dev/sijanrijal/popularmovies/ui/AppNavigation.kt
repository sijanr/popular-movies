package dev.sijanrijal.popularmovies.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import coil.annotation.ExperimentalCoilApi
import dev.sijanrijal.popularmovies.home.ui.HomeScreen
import dev.sijanrijal.popularmovies.home.viewmodel.HomeViewModel
import dev.sijanrijal.popularmovies.navigation.ScreenRouteNavigation
import dev.sijanrijal.popularmovies.screens.search.ui.SearchScreen
import dev.sijanrijal.popularmovies.screens.search.viewmodel.SearchViewModel

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
        val searchViewModel = hiltViewModel<SearchViewModel>()
        SearchScreen(viewModel = searchViewModel, modifier = Modifier.padding(16.dp))
    }
}