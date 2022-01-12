package dev.sijanrijal.popularmovies.screens.search.repository

import dev.sijanrijal.popularmovies.core.network.services.popular.PopularApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val popularApiService: PopularApiService
): SearchRepository {

    // Get the response from popular movies api
    override suspend fun getPopularMovies(): Result {
        return withContext(Dispatchers.IO) {
            val response = popularApiService.getPopularMovies()
            if (response.isSuccessful) {
                Result.Success(response.body()!!)
            } else {
                Result.Failure
            }
        }
    }
}