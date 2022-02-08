package dev.sijanrijal.popularmovies.core.network.services.upcoming

import dev.sijanrijal.popularmovies.core.network.UrlProvider
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface UpcomingMoviesApiService {
    @GET("movie/upcoming")
    suspend fun getUpcomingMovies(
        @Query("api_key") apiKey: String = UrlProvider.API_KEY
    ): Response<UpcomingMoviesNetworkResponse>
}