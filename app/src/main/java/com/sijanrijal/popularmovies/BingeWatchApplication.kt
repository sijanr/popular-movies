package com.sijanrijal.popularmovies

import android.app.Application
import com.sijanrijal.popularmovies.network.MovieApiService
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class BingeWatchApplication: Application() {
    @Inject lateinit var moviesApi: MovieApiService
}