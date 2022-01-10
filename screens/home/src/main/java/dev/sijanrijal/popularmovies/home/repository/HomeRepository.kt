package dev.sijanrijal.popularmovies.home.repository

interface HomeRepository {
    suspend fun getNowPlayingMovies(): Result
}