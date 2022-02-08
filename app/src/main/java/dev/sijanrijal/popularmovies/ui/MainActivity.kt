package dev.sijanrijal.popularmovies.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.sijanrijal.popularmovies.ui.screens.common.AppContainer

@AndroidEntryPoint
class MainActivity: AppCompatActivity() {

    @ExperimentalUnitApi
    @ExperimentalMaterialApi
    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            AppContainer(navController) { paddingValues ->
                AppNavigation(modifier = Modifier
                    .padding(bottom = paddingValues.calculateBottomPadding())
                    .fillMaxSize(), navController)
            }
        }
    }
}