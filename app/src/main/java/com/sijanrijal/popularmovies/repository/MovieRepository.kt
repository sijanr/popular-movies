package com.sijanrijal.popularmovies.repository

import com.sijanrijal.popularmovies.network.Result

interface MovieRepository {
    suspend fun getNowPlayingMovies(): Result
}