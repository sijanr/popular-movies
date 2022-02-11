package dev.sijanrijal.popularmovies.home

import dev.sijanrijal.popularmovies.core.network.UrlProvider
import dev.sijanrijal.popularmovies.core.network.services.nowplaying.NowPlayingMovie
import dev.sijanrijal.popularmovies.core.network.services.popular.PopularMovies
import dev.sijanrijal.popularmovies.home.repository.HomeRepository
import dev.sijanrijal.popularmovies.home.repository.Result
import dev.sijanrijal.popularmovies.home.viewmodel.HomeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest {

    @Mock
    private lateinit var homeRepository: HomeRepository

    private val testCoroutineDispatcher = TestCoroutineDispatcher()

    private lateinit var viewModel: HomeViewModel

    @Before
    fun setup() {
        viewModel = HomeViewModel(urlProvider = UrlProvider, movieRepository = homeRepository)
        Dispatchers.setMain(testCoroutineDispatcher)
    }

    @After
    fun reset() {
        Dispatchers.resetMain()
        testCoroutineDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun homeViewModel_fetchNowPlayingMovies_success() {
        testCoroutineDispatcher.runBlockingTest {
            `when`(homeRepository.getNowPlayingMovies()).thenReturn(Result.Success(popularMoviesList))
            viewModel.fetchNowPlayingMovies()
            verify(homeRepository, times(1)).getNowPlayingMovies()
            assertEquals(popularMoviesList, viewModel.nowPlayingMovies.value.moviesList)
        }
    }

    @Test
    fun homeViewModel_fetchNowPlayingMovies_failure() {
        testCoroutineDispatcher.runBlockingTest {
            `when`(homeRepository.getNowPlayingMovies()).thenReturn(Result.Failure)
            viewModel.fetchNowPlayingMovies()
            verify(homeRepository, times(1)).getNowPlayingMovies()
            assertEquals(emptyList<NowPlayingMovie>(), viewModel.nowPlayingMovies.value.moviesList)
        }
    }

    private companion object {
        val popularMoviesList = listOf(
            NowPlayingMovie(
                posterUrl = null,
                overview = "Popular movies sample overview",
                releaseDate = "",
                title = "Popular movies sample title",
                rating = 9f,
                genre = null,
                movieBackDropImagePath = null,
                id = null
            )
        )
    }
}