package dev.sijanrijal.popularmovies.home

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BingeWatchApplication: Application() {
    override fun onCreate() {
        super.onCreate()
    }
}