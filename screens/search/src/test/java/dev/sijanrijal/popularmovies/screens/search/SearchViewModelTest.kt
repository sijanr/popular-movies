package dev.sijanrijal.popularmovies.screens.search

import app.cash.turbine.test
import dev.sijanrijal.popularmovies.core.network.services.popular.PopularMovies
import dev.sijanrijal.popularmovies.screens.search.model.TopRatedMovies
import dev.sijanrijal.popularmovies.screens.search.model.UpcomingMovies
import dev.sijanrijal.popularmovies.screens.search.repository.SearchRepository
import dev.sijanrijal.popularmovies.screens.search.viewmodel.SearchUIState
import dev.sijanrijal.popularmovies.screens.search.viewmodel.SearchViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Test
import org.junit.Before
import org.junit.Assert.*
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SearchViewModelTest {

    @Mock
    private lateinit var searchRepository: SearchRepository

    private val testCoroutineDispatcher = TestCoroutineDispatcher()

    private lateinit var viewModel: SearchViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testCoroutineDispatcher)
    }

    @After
    fun reset() {
        Dispatchers.resetMain()
        testCoroutineDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun searchViewModel_fetchMovies_success() {
        testCoroutineDispatcher.runBlockingTest {
            `when`(searchRepository.getPopularMovies()).thenReturn(flowOf(popularMovies))
            `when`(searchRepository.getTopRatedMovies()).thenReturn(flowOf(topRatedMovies))
            `when`(searchRepository.getUpcomingMovies()).thenReturn(flowOf(upcomingMovies))
            viewModel = SearchViewModel(searchRepository = searchRepository)
            verify(searchRepository, times(1)).getPopularMovies()
            verify(searchRepository, times(1)).getTopRatedMovies()
            verify(searchRepository, times(1)).getUpcomingMovies()
            val uiState = SearchUIState(popularMovies, topRatedMovies, upcomingMovies)
            viewModel.searchMoviesState.test {
                assertEquals(uiState, expectMostRecentItem())
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun searchViewModel_fetchMovies_failure() {
        testCoroutineDispatcher.runBlockingTest {
            `when`(searchRepository.getPopularMovies()).thenReturn(flowOf(emptyList()))
            `when`(searchRepository.getTopRatedMovies()).thenReturn(flowOf(emptyList()))
            `when`(searchRepository.getUpcomingMovies()).thenReturn(flowOf(emptyList()))
            viewModel = SearchViewModel(searchRepository = searchRepository)
            verify(searchRepository, times(1)).getPopularMovies()
            verify(searchRepository, times(1)).getTopRatedMovies()
            verify(searchRepository, times(1)).getUpcomingMovies()
            viewModel.searchMoviesState.test {
                assertEquals(SearchUIState(), expectMostRecentItem())
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    private companion object {
        val popularMovies = listOf(
            PopularMovies(
                backdropUrl = "",
                overview = "Sample data overview",
                rating = 7f,
                posterUrl = null,
                releaseDate = "",
                title = "Sample movie detail",
                movieId = 12
            )
        )

        val topRatedMovies = listOf(
            TopRatedMovies(
                overview = "Sample data overview",
                rating = 7f,
                posterUrl = null,
                releaseDate = "",
                movieTitle = "Sample movie detail",
                id = 12
            )
        )

        val upcomingMovies = listOf(
            UpcomingMovies(
                overview = "Sample data overview",
                rating = 7f,
                posterUrl = null,
                releaseDate = "",
                movieTitle = "Sample movie detail",
                id = 12
            )
        )
    }
}