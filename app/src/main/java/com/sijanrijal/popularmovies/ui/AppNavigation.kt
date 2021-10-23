package com.sijanrijal.popularmovies.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import coil.annotation.ExperimentalCoilApi
import com.sijanrijal.popularmovies.ui.screens.HomeScreen

internal enum class Screen(val route: String) {
    Home("home"),
    Search("search")
}

@Composable
internal fun AppNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    NavHost(modifier = modifier, navController = navController, startDestination = Screen.Home.route) {
        addHome()
        addSearch()
    }
}

@ExperimentalCoilApi
private fun NavGraphBuilder.addHome() {
    composable(Screen.Home.route) {
        HomeScreen()
    }
}

private fun NavGraphBuilder.addSearch() {
    composable(Screen.Search.route) {
        Text(text = "Search screen", color = LocalContentColor.current, style = MaterialTheme.typography.h3)
    }
}