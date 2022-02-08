package dev.sijanrijal.popularmovies.home.ui

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import coil.transform.RoundedCornersTransformation
import dev.sijanrijal.popularmovies.core.network.UrlProvider
import dev.sijanrijal.popularmovies.home.viewmodel.HomeViewModel
import kotlin.math.abs
import kotlin.math.roundToInt

@ExperimentalCoilApi
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel,
    onMovieClicked: (movieId: Long) -> Unit
) {
    val urlProvider = viewModel.urlProvider
    val movieList = viewModel.nowPlayingMovies.collectAsState().value.moviesList
    viewModel.fetchNowPlayingMovies()
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val movieContentWidthRatio = screenWidth * 0.674f
    var offsetState by remember {
        mutableStateOf(0f)
    }
    val scrollableController = rememberScrollableState {
        when {
            offsetState + it < -movieList.lastIndex * screenWidth.value * 0.6f -> {
                offsetState = -movieList.lastIndex * screenWidth.value * 0.6f
                0f
            }
            offsetState + it > 0 -> {
                offsetState = 0f
                0f
            }
            else -> {
                offsetState += it
                it
            }
        }
    }
    val indexFraction = abs(offsetState) / (screenWidth.value * 0.6f)
    val flingBehavior = object : FlingBehavior {
        override suspend fun ScrollScope.performFling(initialVelocity: Float): Float {
            return movieContentWidthRatio.value
        }

    }
    Box(
        modifier = modifier.scrollable(
            orientation = Orientation.Horizontal,
            state = scrollableController,
            flingBehavior = flingBehavior
        )
    ) {
        movieList.forEachIndexed { index, movie ->
            val opacity = if (indexFraction.roundToInt() == index) 1f else 0f
            val shape = when {
                (index > indexFraction || indexFraction < index + 1) -> RectangleShape
                else -> FractionalRectangle(index - indexFraction, index + 1f - indexFraction)
            }
            FullSizeMoviePoster(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(0.64f),
                url = movie.movieBackDropImagePath,
                alpha = opacity,
                urlProvider = urlProvider
            )
        }
        Spacer(
            modifier = Modifier
                .background(brush = Brush.verticalGradient(listOf(Color.Transparent, Color.Black)))
                .align(Alignment.BottomEnd)
                .fillMaxWidth()
                .fillMaxHeight(0.4f)
        )
        Spacer(
            modifier = Modifier
                .background(
                    brush = Brush.verticalGradient(
                        listOf(
                            MaterialTheme.colors.background.copy(
                                alpha = 0.4f
                            ), Color.Transparent
                        )
                    )
                )
                .fillMaxWidth()
                .fillMaxHeight(0.25f)
        )
        movieList.forEachIndexed { index, movie ->
            val center = screenWidth * 0.6f * index
            val distanceFromCenter = abs(offsetState + center.value) / (screenWidth.value * 0.6f)
            val animateOffset by animateDpAsState(targetValue = if (index == indexFraction.roundToInt()) 0.dp else 34.dp)
            MovieContent(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .offset(
                        x = offsetState.dp + center,
                        y = lerp((-75).dp, 80.dp, distanceFromCenter)
                    )
                    .width(movieContentWidthRatio)
                    .background(Color.Transparent)
                    .clickable {
                        movie.id?.let(onMovieClicked)
                    },
                movieName = movie.title,
                posterUrl = movie.posterUrl,
                urlProvider = urlProvider
            )
        }
        Text(
            text = "Now Playing",
            style = MaterialTheme.typography.h3,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 2.dp),
            color = MaterialTheme.colors.onSurface
        )
    }
}


fun FractionalRectangle(startFraction: Float, endFraction: Float) = object : Shape {
    override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density) =
        Outline.Rectangle(
            Rect(
                top = 0f,
                left = startFraction * size.width,
                bottom = 0f,
                right = endFraction * size.width
            )
        )
}

@ExperimentalCoilApi
@Composable
fun MovieContent(
    modifier: Modifier = Modifier,
    movieName: String?,
    posterUrl: String?,
    urlProvider: UrlProvider
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        SmallMoviePoster(
            url = posterUrl, modifier = Modifier
                .width(180.dp)
                .aspectRatio(0.627f),
            urlProvider = urlProvider
        )
        Text(
            text = movieName ?: "Not Available",
            style = MaterialTheme.typography.body2,
            color = Color.White,
            textAlign = TextAlign.Center
        )
    }
}

@ExperimentalCoilApi
@Composable
fun FullSizeMoviePoster(
    modifier: Modifier = Modifier,
    url: String?,
    alpha: Float,
    urlProvider: UrlProvider
) {
    val animateAlpha by animateFloatAsState(
        targetValue = alpha,
        animationSpec = tween(400, easing = LinearEasing)
    )
    Image(
        painter = rememberImagePainter(urlProvider.imageUrl + "/original$url"),
        contentDescription = null,
        modifier = modifier,
        contentScale = ContentScale.Crop,
        alpha = animateAlpha
    )
}

@ExperimentalCoilApi
@Composable
fun SmallMoviePoster(modifier: Modifier = Modifier, url: String?, urlProvider: UrlProvider) {
    Image(
        painter = rememberImagePainter(urlProvider.imageUrl + "/w500$url", builder = {
            transformations(RoundedCornersTransformation(88.dp.value))
        }), contentDescription = null, modifier = modifier
    )
}