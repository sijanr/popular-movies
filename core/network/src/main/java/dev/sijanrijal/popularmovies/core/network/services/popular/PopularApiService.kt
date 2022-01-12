package dev.sijanrijal.popularmovies.core.network.services.popular

import dev.sijanrijal.popularmovies.core.network.UrlProvider
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PopularApiService {

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String = UrlProvider.API_KEY,
        @Query("page") pageNumber: Int = 1,
    ): Response<PopularMoviesResponse>

}