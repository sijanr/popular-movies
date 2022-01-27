package dev.sijanrijal.popularmovies.navigation

sealed class MainNavigationScreens(private val route: String): ScreenRoute {
    override fun route(): String {
        return route
    }
    object Home: MainNavigationScreens("Home")
    object Search: MainNavigationScreens("Search")
}

sealed class LeafNavigationScreens(private val route: String) {

    fun createRoute(screen: MainNavigationScreens) = "${screen.route()}/$route"

    object Details: LeafNavigationScreens("movie/{movieId}") {
        fun createRoute(screen: MainNavigationScreens, movieId: Long): String {
            return "${screen.route()}/movie/$movieId"
        }
    }
}