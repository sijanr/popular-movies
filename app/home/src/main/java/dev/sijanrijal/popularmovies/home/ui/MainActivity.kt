package dev.sijanrijal.popularmovies.home.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import coil.annotation.ExperimentalCoilApi
import dagger.hilt.android.AndroidEntryPoint
import dev.sijanrijal.popularmovies.home.network.UrlProvider
import dev.sijanrijal.popularmovies.home.ui.screens.common.AppContainer
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity: AppCompatActivity() {

    @Inject
    lateinit var urlProvider: UrlProvider

    @ExperimentalCoilApi
    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            AppContainer(navController) { paddingValues ->
                AppNavigation(modifier = Modifier
                    .padding(bottom = paddingValues.calculateBottomPadding() / 2)
                    .fillMaxSize(), navController, urlProvider)
            }
        }
    }
}