package dev.sijanrijal.popularmovies

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BingeWatchApplication: Application() {
    override fun onCreate() {
        super.onCreate()
    }
}