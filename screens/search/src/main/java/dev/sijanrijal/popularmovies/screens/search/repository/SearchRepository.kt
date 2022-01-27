package dev.sijanrijal.popularmovies.screens.search.repository

interface SearchRepository {
    suspend fun getPopularMovies(): Result
}