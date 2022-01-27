package dev.sijanrijal.popularmovies.core.network.services.genre

import retrofit2.Response
import retrofit2.http.GET

interface TMDBGenreApiService {
    @GET("genre/movie/list")
    suspend fun getTMDBMovieGenres(): Response<TMDBMovieGenreResponse>
}