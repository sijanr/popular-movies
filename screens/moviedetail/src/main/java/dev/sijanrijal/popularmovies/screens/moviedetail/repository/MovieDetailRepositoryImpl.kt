package dev.sijanrijal.popularmovies.screens.moviedetail.repository

import dev.sijanrijal.popularmovies.core.network.services.moviedetail.MovieDetailApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MovieDetailRepositoryImpl @Inject constructor(
    private val movieDetailApiService: MovieDetailApiService
): MovieDetailRepository {

    override suspend fun getMovieDetail(movieId: Long): Result = withContext(Dispatchers.IO) {
        val response = movieDetailApiService.getMovieDetails(movieId = movieId)
        when {
            response.isSuccessful -> Result.Success(response.body()!!)
            else -> Result.Failure
        }
    }
}