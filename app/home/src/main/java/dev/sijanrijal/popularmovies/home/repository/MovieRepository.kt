package dev.sijanrijal.popularmovies.home.repository

import dev.sijanrijal.popularmovies.home.network.Result

interface MovieRepository {
    suspend fun getNowPlayingMovies(): Result
}