package dev.sijanrijal.popularmovies.screens.moviedetail.repository

import dev.sijanrijal.popularmovies.core.network.services.moviedetail.MovieDetail

sealed class Result {
    class Success(val result: MovieDetail): Result()
    object Failure: Result()
}