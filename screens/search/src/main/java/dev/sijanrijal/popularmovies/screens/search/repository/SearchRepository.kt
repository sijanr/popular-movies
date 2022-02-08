package dev.sijanrijal.popularmovies.screens.search.repository

import dev.sijanrijal.popularmovies.core.network.services.popular.PopularMovies
import dev.sijanrijal.popularmovies.screens.search.model.MovieSearch
import dev.sijanrijal.popularmovies.screens.search.model.TopRatedMovies
import dev.sijanrijal.popularmovies.screens.search.model.UpcomingMovies
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    /**
     * Get a list of popular movies
     * */
    fun getPopularMovies(): Flow<List<PopularMovies>>

    /**
     * Get a list of top rated movies
     * */
    fun getTopRatedMovies(): Flow<List<TopRatedMovies>>

    /**
     * Get a list of upcoming movies
     * */
    fun getUpcomingMovies(): Flow<List<UpcomingMovies>>

    /**
     * Get a list of movies that match the keywords
     * */
    suspend fun searchMovie(keywords: String): List<MovieSearch>
}