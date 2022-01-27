package dev.sijanrijal.popularmovies.commonrepository.genre

import dev.sijanrijal.popularmovies.core.network.services.genre.TMDBMovieGenre

interface MovieGenreRepository {
    suspend fun getAllTMDBMovieGenres(genreId: List<Long>): List<String>
}