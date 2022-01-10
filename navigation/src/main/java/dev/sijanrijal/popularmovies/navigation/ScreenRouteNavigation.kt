package dev.sijanrijal.popularmovies.navigation

sealed class ScreenRouteNavigation(private val route: String): ScreenRoute {
    override fun route(): String {
        return route
    }
    object Home: ScreenRouteNavigation("Home")
    object Search: ScreenRouteNavigation("Search")
}