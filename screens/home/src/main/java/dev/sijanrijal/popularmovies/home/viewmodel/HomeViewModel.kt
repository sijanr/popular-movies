package dev.sijanrijal.popularmovies.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.sijanrijal.popularmovies.home.repository.HomeRepository
import dev.sijanrijal.popularmovies.core.network.UrlProvider
import dev.sijanrijal.popularmovies.core.network.services.nowplaying.NowPlayingMovie
import dev.sijanrijal.popularmovies.home.repository.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(val urlProvider: UrlProvider, private val movieRepository: HomeRepository) :
    ViewModel() {

    private val _nowPlayingMovies = MutableStateFlow(MoviesUiState(emptyList()))
    val nowPlayingMovies: StateFlow<MoviesUiState>
        get() = _nowPlayingMovies

    private val _errorFetchingMovies = MutableStateFlow(false)
    val errorFetchingMovies: StateFlow<Boolean>
        get() = _errorFetchingMovies


    fun fetchNowPlayingMovies() {
        viewModelScope.launch {
            getNowPlayingMovies()
        }
    }

    private suspend fun getNowPlayingMovies() {
        when (val response = movieRepository.getNowPlayingMovies()) {
            is Result.Success -> {
                _nowPlayingMovies.value = MoviesUiState(response.result)
            }
            is Result.Failure -> _errorFetchingMovies.value = true
        }
    }
}

data class MoviesUiState(
    val moviesList: List<NowPlayingMovie>
)