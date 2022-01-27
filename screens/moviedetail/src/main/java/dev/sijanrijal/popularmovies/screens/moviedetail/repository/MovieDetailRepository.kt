package dev.sijanrijal.popularmovies.screens.moviedetail.repository

interface MovieDetailRepository {
    suspend fun getMovieDetail(movieId: Long): Result
}