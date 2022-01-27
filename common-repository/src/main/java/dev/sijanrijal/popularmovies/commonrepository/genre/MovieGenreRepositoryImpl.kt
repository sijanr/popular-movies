package dev.sijanrijal.popularmovies.commonrepository.genre

import dev.sijanrijal.popularmovies.core.network.services.genre.TMDBGenreApiService
import dev.sijanrijal.popularmovies.core.network.services.genre.TMDBMovieGenre
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MovieGenreRepositoryImpl @Inject constructor(
    private val TMDBGenreApiService: TMDBGenreApiService
) : MovieGenreRepository {

    private var tmdbMovieGenres: List<TMDBMovieGenre>? = null

    override suspend fun getAllTMDBMovieGenres(genreId: List<Long>): List<String> {
        return getAllTMDBMovieGenres().filter { it.genreId in genreId }
            .associate { Pair(it.genreId, it.genre) }.values.toList()
    }

    private suspend fun getAllTMDBMovieGenres(): List<TMDBMovieGenre> {
        tmdbMovieGenres = tmdbMovieGenres
            ?: withContext(Dispatchers.IO) {
                val response = TMDBGenreApiService.getTMDBMovieGenres()
                response.body()?.genres
            } ?: emptyList()
        return tmdbMovieGenres!!
    }
}