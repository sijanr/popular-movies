package dev.sijanrijal.popularmovies.core.network.services.search

import dev.sijanrijal.popularmovies.core.network.UrlProvider
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchApiService {
    @GET("search/movie")
    suspend fun searchMovie(@Query("api_key") apiKey: String = UrlProvider.API_KEY, @Query("query") keywords: String): Response<MovieSearchNetworkModel>
}