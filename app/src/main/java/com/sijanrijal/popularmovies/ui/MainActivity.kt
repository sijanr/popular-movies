package com.sijanrijal.popularmovies.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import coil.annotation.ExperimentalCoilApi
import com.sijanrijal.popularmovies.repository.MovieRepositoryImpl
import com.sijanrijal.popularmovies.ui.screens.HomeScreen
import com.sijanrijal.popularmovies.ui.screens.common.AppContainer
import com.sijanrijal.popularmovies.viewmodel.MainViewModel
import com.sijanrijal.popularmovies.viewmodel.MainViewModelFactory

class MainActivity: AppCompatActivity() {

    @ExperimentalCoilApi
    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppContainer { paddingValues, navControler ->
                AppNavigation(modifier = Modifier
                    .padding(bottom = paddingValues.calculateBottomPadding()/2)
                    .fillMaxSize(), navControler)
            }
        }
    }
}