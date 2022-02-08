package dev.sijanrijal.popularmovies.home.repository

import javax.inject.Inject
import dev.sijanrijal.popularmovies.core.network.services.nowplaying.NowPlayingApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HomeRepositoryImpl @Inject constructor(
    private val nowPlayingApi: NowPlayingApiService,
) : HomeRepository {

    override suspend fun getNowPlayingMovies(): Result =
        withContext(Dispatchers.IO) {
            val response = nowPlayingApi.getNowPlayingMovies()
            when {
                response.isSuccessful -> Result.Success(response.body()!!)
                else -> Result.Failure
            }
        }
}