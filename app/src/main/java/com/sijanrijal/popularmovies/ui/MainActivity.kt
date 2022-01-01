package com.sijanrijal.popularmovies.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import coil.annotation.ExperimentalCoilApi
import com.sijanrijal.popularmovies.network.UrlProvider
import com.sijanrijal.popularmovies.ui.screens.common.AppContainer
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity: AppCompatActivity() {

    @Inject lateinit var urlProvider: UrlProvider

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