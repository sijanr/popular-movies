package com.sijanrijal.popularmovies.repository

import com.sijanrijal.popularmovies.BuildConfig
import com.sijanrijal.popularmovies.network.MovieApiService
import com.sijanrijal.popularmovies.network.Result
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val moviesApi: MovieApiService
) : MovieRepository {

    override suspend fun getNowPlayingMovies(): Result {
        val response = moviesApi.getNowPlayingMovies(BuildConfig.API_KEY)
        return when {
            response.isSuccessful -> Result.Success(response.body()!!)
            else -> Result.Failure
        }
    }
}