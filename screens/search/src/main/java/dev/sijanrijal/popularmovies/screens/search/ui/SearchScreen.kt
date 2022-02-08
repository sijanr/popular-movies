package dev.sijanrijal.popularmovies.screens.search.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import dev.sijanrijal.popularmovies.common.ui.theme.Grey400
import dev.sijanrijal.popularmovies.core.network.UrlProvider
import dev.sijanrijal.popularmovies.screens.search.model.MovieSearch
import dev.sijanrijal.popularmovies.screens.search.ui.common.MoviesContainer
import dev.sijanrijal.popularmovies.screens.search.viewmodel.SearchViewModel

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel,
    onMovieSelected: (movieId: Long) -> Unit
) {
    val searchMovies = viewModel.searchMoviesState.collectAsState()
    val searchMovieResult = viewModel.searchMovieResult.collectAsState(emptyList())
    Column(modifier = modifier) {
        //Search bar
        SearchBar(
            modifier = Modifier.fillMaxWidth(),
            onSearchTextChange = { keywords -> viewModel.searchMovie(keywords = keywords) })

        Box(modifier = Modifier
            .fillMaxSize()
            .padding(top = 8.dp)) {
            val lazyColumnState = rememberLazyListState()
            val searchLazyColumnState = rememberLazyListState()
            LazyColumn(modifier = Modifier.fillMaxSize(), state = lazyColumnState) {

                //Main header text
                item {
                    HeaderText(text = "Movies")
                }

                //Popular movies
                item {
                    MoviesContainer(
                        modifier = Modifier.padding(top = 32.dp),
                        title = "Popular",
                        movies = searchMovies.value.popularMovies,
                        movieTitle = { it.title },
                        moviePosterUrl = { it.posterUrl },
                        onMovieSelected = { onMovieSelected(it.movieId) }
                    )
                }

                //Top Rated movies
                item {
                    MoviesContainer(
                        modifier = Modifier.padding(top = 22.dp),
                        title = "Top Rated",
                        movies = searchMovies.value.topRatedMovies,
                        movieTitle = { it.movieTitle },
                        moviePosterUrl = { it.posterUrl },
                        onMovieSelected = { onMovieSelected(it.id) }
                    )
                }

                //Upcoming movies
                item {
                    MoviesContainer(
                        modifier = Modifier.padding(top = 22.dp),
                        title = "Upcoming",
                        movies = searchMovies.value.upcomingMovies,
                        movieTitle = { it.movieTitle },
                        moviePosterUrl = { it.posterUrl },
                        onMovieSelected = { onMovieSelected(it.id) }
                    )
                }
            }
            Surface(
                modifier = Modifier.heightIn(min = 0.dp, max = 320.dp),
                shape = RoundedCornerShape(8.dp),
                elevation = 4.dp,
                border = BorderStroke(
                    width = 1.dp,
                    color = MaterialTheme.colors.onBackground.copy(alpha = 0.4f)
                )
            ) {
                LazyColumn(modifier = Modifier.fillMaxWidth(), state = searchLazyColumnState) {
                    items(searchMovieResult.value) { item: MovieSearch ->
                        Text(
                            text = item.movieTitle,
                            modifier = Modifier
                                .clickable { onMovieSelected(item.movieID.toLong()) }
                                .padding(bottom = 8.dp)
                                .padding(horizontal = 4.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
internal fun HeaderText(modifier: Modifier = Modifier, text: String) {
    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.h4,
        fontWeight = FontWeight.Bold
    )
}