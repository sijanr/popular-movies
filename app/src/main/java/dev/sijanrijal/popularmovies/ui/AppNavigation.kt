package dev.sijanrijal.popularmovies.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import coil.annotation.ExperimentalCoilApi
import dev.sijanrijal.popularmovies.home.ui.HomeScreen
import dev.sijanrijal.popularmovies.home.viewmodel.HomeViewModel
import dev.sijanrijal.popularmovies.navigation.LeafNavigationScreens
import dev.sijanrijal.popularmovies.navigation.MainNavigationScreens
import dev.sijanrijal.popularmovies.screens.moviedetail.ui.MovieDetailScreen
import dev.sijanrijal.popularmovies.screens.search.ui.SearchScreen
import dev.sijanrijal.popularmovies.screens.search.viewmodel.SearchViewModel

@ExperimentalMaterialApi
@ExperimentalUnitApi
@Composable
internal fun AppNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = MainNavigationScreens.Home.route()
    ) {
        addHome(navController)
        addSearch(navController)
        addMovieDetails(MainNavigationScreens.Home, navController)
        addMovieDetails(MainNavigationScreens.Search, navController)
    }
}

@ExperimentalCoilApi
private fun NavGraphBuilder.addHome(navController: NavHostController) {
    composable(MainNavigationScreens.Home.route()) {
        val viewModel = hiltViewModel<HomeViewModel>()
        HomeScreen(viewModel = viewModel, onMovieClicked = { movieId ->
            navController.navigate(
                LeafNavigationScreens.Details.createRoute(
                    MainNavigationScreens.Home,
                    movieId
                )
            )
        })
    }
}

private fun NavGraphBuilder.addSearch(navController: NavHostController) {
    composable(MainNavigationScreens.Search.route()) {
        val searchViewModel = hiltViewModel<SearchViewModel>()
        SearchScreen(
            viewModel = searchViewModel,
            modifier = Modifier.padding(16.dp).fillMaxSize(),
            onMovieSelected = { movieId ->
                navController.navigate(
                    LeafNavigationScreens.Details.createRoute(
                        MainNavigationScreens.Search,
                        movieId
                    )
                )
            })
    }
}

@ExperimentalUnitApi
@ExperimentalMaterialApi
private fun NavGraphBuilder.addMovieDetails(
    root: MainNavigationScreens,
    navController: NavHostController
) {
    composable(
        route = LeafNavigationScreens.Details.createRoute(root),
        arguments = listOf(
            navArgument(name = "movieId") {
                type = NavType.LongType
            }
        )
    ) { navBackStackEntry ->
        val movieId = navBackStackEntry.arguments?.getLong("movieId")
        if (movieId != null) {
            MovieDetailScreen(movieId = movieId, onNavigateUp = navController::navigateUp)
        }
    }
}