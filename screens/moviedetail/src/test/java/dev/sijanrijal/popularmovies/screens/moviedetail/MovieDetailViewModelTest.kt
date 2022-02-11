package dev.sijanrijal.popularmovies.screens.moviedetail

import dev.sijanrijal.popularmovies.core.network.services.moviedetail.MovieDetail
import dev.sijanrijal.popularmovies.screens.moviedetail.repository.MovieDetailRepository
import dev.sijanrijal.popularmovies.screens.moviedetail.repository.Result
import dev.sijanrijal.popularmovies.screens.moviedetail.viewmodel.MovieDetailViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Test
import org.junit.Assert.*
import org.junit.Before
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MovieDetailViewModelTest {

    @Mock
    private lateinit var movieDetailRepository: MovieDetailRepository

    private val testCoroutineDispatcher = TestCoroutineDispatcher()

    private lateinit var viewModel: MovieDetailViewModel

    @Before
    fun setup() {
        viewModel = MovieDetailViewModel(movieDetailRepository = movieDetailRepository)
        Dispatchers.setMain(testCoroutineDispatcher)
    }

    @After
    fun resetAll() {
        Dispatchers.resetMain()
        testCoroutineDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun movieDetailViewModel_getMovie_success() {
       testCoroutineDispatcher.runBlockingTest {
            `when`(movieDetailRepository.getMovieDetail(anyLong())).thenReturn(Result.Success(movieDetailSampleData))
            viewModel.getMovie(12L)
            verify(movieDetailRepository, times(1)).getMovieDetail(12L)
            assertEquals(movieDetailSampleData, viewModel.movieDetailState.value)
        }
    }

    @Test
    fun movieDetailViewModel_getMovie_failure() {
        testCoroutineDispatcher.runBlockingTest {
            `when`(movieDetailRepository.getMovieDetail(anyLong())).thenReturn(Result.Failure)
            viewModel.getMovie(12L)
            verify(movieDetailRepository, times(1)).getMovieDetail(12L)
            assertEquals(null, viewModel.movieDetailState.value)
        }
    }

    private companion object {
        val movieDetailSampleData = MovieDetail(
            backdropUrl = null,
            overview = "Sample data overview",
            rating = 7f,
            posterUrl = null,
            releaseDate = "",
            movieRuntime = null,
            title = "Sample movie detail",
            genres = emptyList()
        )
    }
}