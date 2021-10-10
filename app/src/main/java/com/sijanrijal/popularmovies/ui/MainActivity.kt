package com.sijanrijal.popularmovies.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.sijanrijal.popularmovies.repository.MovieRepositoryImpl
import com.sijanrijal.popularmovies.ui.screens.HomeScreen
import com.sijanrijal.popularmovies.ui.screens.common.PopularMoviesTheme
import com.sijanrijal.popularmovies.viewmodel.MainViewModel
import com.sijanrijal.popularmovies.viewmodel.MainViewModelFactory

class MainActivity: AppCompatActivity() {

    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel: MainViewModel = ViewModelProvider(this, MainViewModelFactory(MovieRepositoryImpl(this))).get(MainViewModel::class.java)
        setContent {
            PopularMoviesTheme {
                HomeScreen(modifier = Modifier.padding(it).fillMaxSize(), viewModel = viewModel)
            }
        }
    }
}