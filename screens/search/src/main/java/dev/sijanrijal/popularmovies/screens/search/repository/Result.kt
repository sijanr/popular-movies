package dev.sijanrijal.popularmovies.screens.search.repository

import dev.sijanrijal.popularmovies.core.network.services.popular.PopularMoviesResponse

sealed class Result {
    class Success(val result: PopularMoviesResponse): Result()
    object Failure: Result()
}