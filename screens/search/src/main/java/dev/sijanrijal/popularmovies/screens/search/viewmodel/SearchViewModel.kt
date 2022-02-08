package dev.sijanrijal.popularmovies.screens.search.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.sijanrijal.popularmovies.core.network.services.popular.PopularMovies
import dev.sijanrijal.popularmovies.screens.search.model.TopRatedMovies
import dev.sijanrijal.popularmovies.screens.search.model.UpcomingMovies
import dev.sijanrijal.popularmovies.screens.search.repository.SearchRepository
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRepository: SearchRepository
) : ViewModel() {

    //state that holds a list of popular, top rated movies
    val searchMoviesState = combine(
        searchRepository.getTopRatedMovies(),
        searchRepository.getPopularMovies(),
        searchRepository.getUpcomingMovies()
    ) { topRatedMovies, popularMovies, upcomingMovies ->
        SearchUIState(
            popularMovies = popularMovies,
            topRatedMovies = topRatedMovies,
            upcomingMovies = upcomingMovies
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SearchUIState()
    )

    //state flow for search keywords from the user
    private var searchMovie = MutableStateFlow("")
    val searchMovieResult = searchMovie.asStateFlow()
        .debounce(700)
        .mapLatest {
            if (it.isEmpty() || it.length <= 3) {
                emptyList()
            } else {
                searchRepository.searchMovie(it)
            }
        }

    fun searchMovie(keywords: String) {
        searchMovie.value = keywords
    }
}

data class SearchUIState(
    val popularMovies: List<PopularMovies> = emptyList(),
    val topRatedMovies: List<TopRatedMovies> = emptyList(),
    val upcomingMovies: List<UpcomingMovies> = emptyList()
)