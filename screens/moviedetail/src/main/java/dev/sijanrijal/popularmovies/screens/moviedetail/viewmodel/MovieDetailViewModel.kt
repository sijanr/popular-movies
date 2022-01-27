package dev.sijanrijal.popularmovies.screens.moviedetail.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.sijanrijal.popularmovies.core.network.services.moviedetail.MovieDetail
import dev.sijanrijal.popularmovies.screens.moviedetail.repository.MovieDetailRepository
import dev.sijanrijal.popularmovies.screens.moviedetail.repository.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val movieDetailRepository: MovieDetailRepository,
): ViewModel(){
    private var _movieDetailState: MutableStateFlow<MovieDetail?> = MutableStateFlow(null)
    val movieDetailState: StateFlow<MovieDetail?>
        get() = _movieDetailState

    fun getMovie(movieId: Long) {
        viewModelScope.launch {
            when(val movie = movieDetailRepository.getMovieDetail(movieId = movieId)) {
                is Result.Success -> _movieDetailState.value = movie.result
            }
        }
    }
}