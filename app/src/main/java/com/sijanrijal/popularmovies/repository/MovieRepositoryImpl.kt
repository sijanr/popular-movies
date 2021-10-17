package com.sijanrijal.popularmovies.repository

import android.content.Context
import com.sijanrijal.popularmovies.R
import com.sijanrijal.popularmovies.network.Result
import com.sijanrijal.popularmovies.network.moviesApi

class MovieRepositoryImpl(private val context: Context): MovieRepository {

    override suspend fun getNowPlayingMovies(): Result {
        val response = moviesApi.getNowPlayingMovies(context.getString(R.string.API_KEY))
        return when {
            response.isSuccessful -> Result.Success(response.body()!!)
            else -> Result.Failure
        }
    }
}