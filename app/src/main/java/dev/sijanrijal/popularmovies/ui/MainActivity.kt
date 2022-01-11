package dev.sijanrijal.popularmovies.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.sijanrijal.popularmovies.ui.screens.common.AppContainer

@AndroidEntryPoint
class MainActivity: AppCompatActivity() {

    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            AppContainer(navController) { paddingValues ->
                AppNavigation(modifier = Modifier
                    .padding(bottom = paddingValues.calculateBottomPadding() / 2)
                    .fillMaxSize(), navController)
            }
        }
    }
}