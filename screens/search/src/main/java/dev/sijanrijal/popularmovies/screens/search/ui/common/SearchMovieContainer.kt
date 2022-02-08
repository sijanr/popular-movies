package dev.sijanrijal.popularmovies.screens.search.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
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

@Composable
internal fun <T> MoviesContainer(
    modifier: Modifier = Modifier,
    title: String,
    movies: List<T>,
    movieTitle: (T) -> String,
    moviePosterUrl: (T) -> String?,
    onMovieSelected: (movie: T) -> Unit
) {
    if (movies.isNotEmpty()) {
        Column(modifier = modifier) {
            MovieTypeContainer(title = title)
            val listState = rememberLazyListState()
            LazyRow(
                state = listState,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(top = 22.dp)
            ) {
                items(movies) { movie ->
                    MovieItem(
                        modifier = Modifier.width(IntrinsicSize.Min),
                        movie = movie,
                        movieTitle = movieTitle(movie),
                        moviePosterUrl = moviePosterUrl(movie),
                        onMovieSelected = onMovieSelected
                    )
                }
            }
        }
    }
}

@Composable
internal fun <T> MovieItem(
    modifier: Modifier = Modifier,
    movie: T,
    movieTitle: String,
    moviePosterUrl: String?,
    onMovieSelected: (movie: T) -> Unit
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        MovieImage(
            modifier = Modifier
                .padding(bottom = 8.dp)
                .clickable { onMovieSelected(movie) },
            imageUrl = UrlProvider.imageUrl + "/w500$moviePosterUrl"
        )
        MovieTitleText(text = movieTitle, modifier = Modifier.padding(horizontal = 4.dp))
    }
}

@ExperimentalCoilApi
@Composable
internal fun MovieImage(
    modifier: Modifier = Modifier,
    imageUrl: String,
    imageShape: Shape = RoundedCornerShape(12.dp)
) {
    Image(
        painter = rememberImagePainter(
            data = imageUrl,
            builder = {
                crossfade(enable = true)
            }
        ),
        modifier = modifier
            .clip(imageShape)
            .fillMaxWidth()
            .size(width = 160.dp, height = 230.dp),
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
internal fun MovieTypeContainer(modifier: Modifier = Modifier, title: String) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier) {
        MovieTypeText(text = title)
        TitleLineBar(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .align(Alignment.CenterVertically)
        )
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
internal fun TitleLineBar(
    modifier: Modifier = Modifier,
    barWidth: Dp = 88.dp,
    barHeight: Dp = 2.dp,
    barColor: Color = Grey400,
    barShape: Shape = RoundedCornerShape(50)
) {
    Divider(
        modifier = modifier
            .width(barWidth)
            .height(barHeight)
            .background(color = barColor, shape = barShape)
    )
}