package dev.sijanrijal.popularmovies.core.network.services.moviedetail

import dev.sijanrijal.popularmovies.core.network.BuildConfig
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieDetailApiService {
    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Long,
        @Query("api_key") apiKey: String = BuildConfig.API_KEY
    ): Response<MovieDetail>
}