package dev.sijanrijal.popularmovies.home.repository

import dev.sijanrijal.popularmovies.core.network.services.nowplaying.NowPlayingMovie

sealed class Result {
    class Success(val result: List<NowPlayingMovie>): Result()
    object Failure: Result()
}