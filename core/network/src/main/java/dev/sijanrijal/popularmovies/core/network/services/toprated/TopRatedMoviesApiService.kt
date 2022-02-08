package dev.sijanrijal.popularmovies.core.network.services.toprated

import dev.sijanrijal.popularmovies.core.network.UrlProvider
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface TopRatedMoviesApiService {
    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("api_key") apiKey: String = UrlProvider.API_KEY,
    ): Response<TopRatedMoviesNetworkResponse>
}