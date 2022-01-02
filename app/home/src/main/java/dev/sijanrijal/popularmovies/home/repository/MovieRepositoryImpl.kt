package dev.sijanrijal.popularmovies.home.repository

import dev.sijanrijal.popularmovies.home.BuildConfig
import dev.sijanrijal.popularmovies.home.network.MovieApiService
import javax.inject.Inject
import dev.sijanrijal.popularmovies.home.network.Result

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