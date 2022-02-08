package dev.sijanrijal.popularmovies.home.repository

import dev.sijanrijal.popularmovies.core.network.services.nowplaying.NowPlayingResponse

sealed class Result {
    class Success(val result: NowPlayingResponse): Result()
    object Failure: Result()
}