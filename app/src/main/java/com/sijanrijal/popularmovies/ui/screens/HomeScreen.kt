package com.sijanrijal.popularmovies.ui.screens

import android.os.Build
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import coil.transform.RoundedCornersTransformation
import com.sijanrijal.popularmovies.network.Movie
import com.sijanrijal.popularmovies.network.imageUrl
import com.sijanrijal.popularmovies.viewmodel.MainViewModel
import com.sijanrijal.popularmovies.viewmodel.MoviesUiState
import kotlin.math.abs
import kotlin.math.roundToInt

data class Movie(val name: String, val url: String)

@ExperimentalCoilApi
@Composable
fun HomeScreen(modifier: Modifier = Modifier, viewModel: MainViewModel) {
    val movieListState = viewModel.nowPlayingMovies.collectAsState()
    val movieListStateValue = movieListState.value
    viewModel.fetchNowPlayingMovies()
    val scrollState = rememberScrollState()
    Column(modifier.scrollable(orientation = Orientation.Vertical, state = scrollState)) {
        Text(
            text = "Trending Movies",
            style = MaterialTheme.typography.h3,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp),
            color = Color.White
        )
        if (movieListStateValue is MoviesUiState.Success) {
            NowPlayingMovieScreen(movieList = movieListStateValue.moviesList)
        }
    }
}

@ExperimentalCoilApi
@Composable
fun NowPlayingMovieScreen(modifier: Modifier = Modifier, movieList: List<Movie>) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val movieContentWidthRatio = screenWidth * 0.674f
    var offsetState by remember {
        mutableStateOf(0f)
    }
    val scrollableController = rememberScrollableState {
        if (offsetState + it < -movieList.lastIndex * screenWidth.value * 0.6f) {
            offsetState = -movieList.lastIndex * screenWidth.value * 0.6f
            0f
        } else if (offsetState + it > 0) {
            offsetState = 0f
            0f
        } else {
            offsetState += it
            it
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
                    .aspectRatio(0.774f),
                url = movie.movieBackDropImagePath,
                alpha = opacity
            )
        }
        Spacer(
            modifier = Modifier
                .background(brush = Brush.verticalGradient(listOf(Color.Transparent, Color.Black)))
                .align(Alignment.BottomEnd)
                .fillMaxWidth()
                .fillMaxHeight(0.4f)
        )
        movieList.forEachIndexed { index, movie ->
            val center = screenWidth * 0.6f * index
            val distanceFromCenter = abs(offsetState + center.value) / (screenWidth.value * 0.6f)
            val animateOffset by animateDpAsState(targetValue = if (index == indexFraction.roundToInt()) 0.dp else 34.dp)
            MovieContent(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .offset(x = offsetState.dp + center, y = lerp((-35).dp, 80.dp, distanceFromCenter))
                    .width(movieContentWidthRatio)
                    .background(Color.Transparent),
                movieName = movie.title,
                posterUrl = movie.posterUrl
            )
        }
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

@Composable
fun MovieContent(modifier: Modifier = Modifier, movieName: String?, posterUrl: String?) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        SmallMoviePoster(url = posterUrl)
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
fun FullSizeMoviePoster(modifier: Modifier = Modifier, url: String?, alpha: Float) {
    val animateAlpha by animateFloatAsState(
        targetValue = alpha,
        animationSpec = tween(400, easing = LinearEasing)
    )
//    if (url!=null) {
    Image(
        painter = rememberImagePainter(imageUrl + "original$url"),
        contentDescription = null,
        modifier = modifier,
        contentScale = ContentScale.Crop,
        alpha = animateAlpha
    )
//    } else {
//        Box(modifier = modifier.background(color = MaterialTheme.colors.surface).alpha(animateAlpha))
//    }
}

@Composable
fun SmallMoviePoster(modifier: Modifier = Modifier, url: String?) {
    Image(
        painter = rememberImagePainter(imageUrl+"w500$url", builder = {
            transformations(RoundedCornersTransformation(88.dp.value))
        }), contentDescription = null, modifier = Modifier
            .width(180.dp)
            .aspectRatio(0.627f)
    )
}