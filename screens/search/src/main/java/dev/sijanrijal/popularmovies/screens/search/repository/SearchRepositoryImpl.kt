package dev.sijanrijal.popularmovies.screens.search.repository

import dev.sijanrijal.popularmovies.core.network.services.popular.PopularApiService
import dev.sijanrijal.popularmovies.core.network.services.popular.PopularMovies
import dev.sijanrijal.popularmovies.core.network.services.search.SearchApiService
import dev.sijanrijal.popularmovies.core.network.services.toprated.TopRatedMoviesApiService
import dev.sijanrijal.popularmovies.core.network.services.upcoming.UpcomingMoviesApiService
import dev.sijanrijal.popularmovies.screens.search.model.MovieSearch
import dev.sijanrijal.popularmovies.screens.search.model.TopRatedMovies
import dev.sijanrijal.popularmovies.screens.search.model.UpcomingMovies
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val popularApiService: PopularApiService,
    private val searchApiService: SearchApiService,
    private val topRatedMoviesApiService: TopRatedMoviesApiService,
    private val upcomingMoviesApiService: UpcomingMoviesApiService
): SearchRepository {

    // Get the response from popular movies api
    override fun getPopularMovies(): Flow<List<PopularMovies>> {
        return flow {
            val response = popularApiService.getPopularMovies()
            if (response.isSuccessful) {
                emit(response.body()!!.movies)
            } else {
                emit(emptyList())
            }
        }.flowOn(Dispatchers.IO)
    }

    //Get a list of to rated movies
    override fun getTopRatedMovies(): Flow<List<TopRatedMovies>> {
        return flow {
            val response = topRatedMoviesApiService.getTopRatedMovies()
            if (response.isSuccessful) {
                val movies = response.body()?.topRatedMovies?.map {
                    TopRatedMovies(
                        it.id,
                        it.posterUrl,
                        it.overview,
                        it.releaseDate,
                        it.movieTitle,
                        it.rating,
                    )
                }
                if (movies!=null && movies.isNotEmpty()) {
                    emit(movies)
                } else {
                    emit(emptyList())
                }
            }
        }.flowOn(Dispatchers.IO)
    }

    //Get upcoming movies
    override fun getUpcomingMovies(): Flow<List<UpcomingMovies>> {
        return flow {
            val response = upcomingMoviesApiService.getUpcomingMovies()
            if (response.isSuccessful) {
                val movies = response.body()?.upcomingMovies?.map {
                    UpcomingMovies(
                        id = it.id,
                        posterUrl = it.posterUrl,
                        overview = it.overview,
                        releaseDate = it.releaseDate,
                        movieTitle = it.movieTitle,
                        rating = it.rating
                    )
                }
                if (movies!=null && movies.isNotEmpty()) {
                    emit(movies)
                } else {
                    emit(emptyList())
                }
            }
        }
    }

    //Get a list of movies that match the keywords
    override suspend fun searchMovie(keywords: String): List<MovieSearch> {
        return withContext(Dispatchers.IO) {
            val response = searchApiService.searchMovie(keywords = keywords)
            if (response.isSuccessful) {
                response.body()?.searchResults?.map {
                    MovieSearch(
                        posterPath = it.posterPath,
                        overview = it.overview,
                        releaseDate = it.releaseDate,
                        movieID = it.movieID,
                        movieTitle = it.movieTitle,
                        rating = it.rating
                    )
                } ?: emptyList()
            } else {
                emptyList()
            }
        }
    }
}