package dev.sijanrijal.popularmovies.screens.search.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import dev.sijanrijal.popularmovies.core.network.services.popular.PopularMovies
import dev.sijanrijal.popularmovies.screens.search.viewmodel.SearchViewModel

@Composable
fun SearchScreen(modifier: Modifier = Modifier, viewModel: SearchViewModel, onMovieSelected: (movieId: Long) -> Unit) {
    val lazyColumnState = rememberLazyListState()
    val searchMovies = viewModel.searchMoviesState.collectAsState()
    LazyColumn(modifier = modifier, state = lazyColumnState) {
        //Main header text
        item {
            HeaderText(text = "Movies")
        }

        //Popular movies
        item {
            PopularMoviesContainer(title = "Popular", movies = searchMovies.value.popularMovies, modifier = Modifier.padding(top = 32.dp), onMovieSelected = onMovieSelected)
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

@Composable
internal fun PopularMoviesContainer(modifier: Modifier = Modifier, title: String, movies: List<PopularMovies>, onMovieSelected: (movieId: Long) -> Unit) {
    Column(modifier = modifier) {
        MovieTypeContainer(title = title)
        val listState = rememberLazyListState()
        LazyRow(state = listState, horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(top = 22.dp)) {
            items(movies) { movie ->
                MovieItemContainer(movie = movie, modifier = Modifier.width(IntrinsicSize.Min), onMovieSelected = onMovieSelected)
            }
        }
    }
}

@Composable
internal fun MovieItemContainer(modifier: Modifier = Modifier, movie: PopularMovies, onMovieSelected: (movieId: Long) -> Unit) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        MovieImage(modifier = Modifier.padding(bottom = 8.dp).clickable{ onMovieSelected(movie.movieId)}, imageUrl = UrlProvider.imageUrl+"/w500${movie.posterUrl}")
        MovieTitleText(text = movie.title, modifier = Modifier.padding(horizontal = 4.dp))
    }
}

@ExperimentalCoilApi
@Composable
internal fun MovieImage(modifier: Modifier = Modifier, imageUrl: String, imageShape: Shape = RoundedCornerShape(12.dp)) {
    Image(
        painter = rememberImagePainter(
            data = imageUrl,
            builder = {
                crossfade(enable = true)
            }
        ),
        modifier = modifier.clip(imageShape).fillMaxWidth().size(width = 200.dp, height = 280.dp),
        contentScale = ContentScale.Crop,
        contentDescription = null
    )
}

@Composable
internal fun MovieTitleText(modifier: Modifier = Modifier, text: String) {
    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.body2,
        textAlign = TextAlign.Center
    )
}

@Composable
internal fun MovieTypeContainer(modifier: Modifier = Modifier, title: String, ) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier) {
        MovieTypeText(text = title)
        TitleLineBar(modifier = Modifier.padding(horizontal = 16.dp).align(Alignment.CenterVertically))
    }
}

@Composable
internal fun MovieTypeText(modifier: Modifier = Modifier, text: String) {
    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.h6,
        fontWeight = FontWeight.Bold
    )
}

@Composable
internal fun TitleLineBar(modifier: Modifier = Modifier, barWidth: Dp = 88.dp, barHeight: Dp = 2.dp, barColor: Color = Grey400, barShape: Shape = RoundedCornerShape(50)) {
    Divider(modifier = modifier.width(barWidth).height(barHeight).background(color = barColor, shape = barShape))
}