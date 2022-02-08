package dev.sijanrijal.popularmovies.core.network.services.nowplaying

import dev.sijanrijal.popularmovies.core.network.UrlProvider
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NowPlayingApiService {
    @GET("movie/now_playing")
    suspend fun getNowPlayingMovies(@Query("api_key") apiKey: String = UrlProvider.API_KEY): Response<NowPlayingResponse>
}