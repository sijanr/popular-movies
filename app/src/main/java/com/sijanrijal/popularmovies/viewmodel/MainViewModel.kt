package com.sijanrijal.popularmovies.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sijanrijal.popularmovies.network.Movie
import com.sijanrijal.popularmovies.network.Result
import com.sijanrijal.popularmovies.repository.MovieRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(private val movieRepository: MovieRepository): ViewModel() {

    private val _nowPlayingMovies = MutableStateFlow(MoviesUiState.Success(emptyList()))
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
        println("Getting movies...")
       val response = withContext(Dispatchers.IO) {
            movieRepository.getNowPlayingMovies()
        }
        when (response) {
            is Result.Success -> {
                val filterList = response.result.movies.filterNotNull()
                _nowPlayingMovies.value = MoviesUiState.Success(filterList)
            }
            is Result.Failure -> _errorFetchingMovies.value = true
        }
    }
}

sealed class MoviesUiState {
    data class Success(val moviesList: List<Movie>): MoviesUiState()
    object Error: MoviesUiState()
}