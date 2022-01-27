package dev.sijanrijal.popularmovies.navigation

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

//abstract class BaseNavigationManager {
//    protected var _commands = MutableStateFlow("")
//    val commands: StateFlow<String>
//        get() = _commands
//
//    abstract fun navigate(direction: ScreenRoute)
//}
//
//class NavigationManager: BaseNavigationManager() {
//    override fun navigate(direction: ScreenRoute) {
//        _commands.value = direction.route()
//    }
//}