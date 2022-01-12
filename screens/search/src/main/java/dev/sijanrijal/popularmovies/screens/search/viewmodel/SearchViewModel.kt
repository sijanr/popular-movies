package dev.sijanrijal.popularmovies.screens.search.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.sijanrijal.popularmovies.core.network.services.popular.PopularMovies
import dev.sijanrijal.popularmovies.screens.search.repository.Result
import dev.sijanrijal.popularmovies.screens.search.repository.SearchRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRepository: SearchRepository
): ViewModel() {
    private var _searchMoviesState: MutableStateFlow<SearchUIState> = MutableStateFlow(SearchUIState())
    val searchMoviesState: StateFlow<SearchUIState>
        get() = _searchMoviesState
    private var _popularMoviesFetchError: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val popularMoviesFetchError: StateFlow<Boolean>
        get() = _popularMoviesFetchError

    init {
        viewModelScope.launch {
            fetchPopularMovies()
        }
    }

    private suspend fun fetchPopularMovies() {
        when(val result = searchRepository.getPopularMovies()) {
            is Result.Success -> _searchMoviesState.value = SearchUIState(popularMovies = result.result.movies)
            is Result.Failure -> _popularMoviesFetchError.value = true
        }
    }
}

data class SearchUIState(
    val popularMovies: List<PopularMovies> = emptyList()
)