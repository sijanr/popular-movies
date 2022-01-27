package dev.sijanrijal.popularmovies.screens.moviedetail.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import dev.sijanrijal.popularmovies.common.ui.theme.Grey500
import dev.sijanrijal.popularmovies.core.network.UrlProvider
import dev.sijanrijal.popularmovies.core.network.services.moviedetail.MovieDetail
import dev.sijanrijal.popularmovies.screens.moviedetail.viewmodel.MovieDetailViewModel

@ExperimentalMaterialApi
@ExperimentalUnitApi
@Composable
fun MovieDetailScreen(movieId: Long, onNavigateUp: () -> Unit) {
    val viewModel = hiltViewModel<MovieDetailViewModel>()
    val movieDetail by viewModel.movieDetailState.collectAsState()
    viewModel.getMovie(movieId)
    movieDetail?.let {
        MovieDetails(it, onNavigateUp)
    }
}

@ExperimentalUnitApi
@ExperimentalMaterialApi
@Composable
internal fun MovieDetails(movieDetail: MovieDetail, onNavigateUp: () -> Unit) {
    val sheetPeekHeight = (LocalConfiguration.current.screenHeightDp*0.45).dp
    BottomSheetScaffold(sheetContent = {
        BottomSheetContent(modifier = Modifier.padding(16.dp), movieDetail = movieDetail)
    }, sheetShape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
    sheetPeekHeight = sheetPeekHeight) {
        MovieDetailScreenContents(movieDetail = movieDetail, onNavigateUp = onNavigateUp)
    }
}

@ExperimentalUnitApi
@Composable
internal fun BottomSheetContent(modifier: Modifier = Modifier, movieDetail: MovieDetail) {
    val genres = movieDetail.genres.joinToString(separator = ", ") { it.genre }
    val movieRunTime = movieDetail.movieRuntime
    val movieRuntimeFormat = if (movieRunTime!=null) {
        when {
            movieRunTime < 60 -> "${movieRunTime}min"
            movieRunTime > 60 -> "${movieRunTime/60}h ${movieRunTime%60}min"
            else -> "1h"
        }
    } else ""
    Column(modifier = modifier) {
        MovieTitleText(movieTitle = movieDetail.title)
        TextWithMediumEmphasis(text = genres, modifier = Modifier.padding(top = 8.dp))
        if (movieRuntimeFormat.isNotEmpty()) {
            TextWithMediumEmphasis(modifier = Modifier.padding(top = 4.dp), text = movieRuntimeFormat)
        }
        MovieOverviewText(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp), overviewText = movieDetail.overview)
    }
}

@Composable
internal fun MovieDetailScreenContents(
    modifier: Modifier = Modifier,
    movieDetail: MovieDetail,
    onNavigateUp: () -> Unit
) {
    Box(modifier = modifier) {
        MoviePoster(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.7f), imageUrl = movieDetail.posterUrl)
        Icon(
            imageVector = Icons.Default.ArrowBackIosNew,
            contentDescription = "Back button",
            tint = MaterialTheme.colors.onBackground,
            modifier = Modifier
                .padding(16.dp)
                .clickable(onClick = onNavigateUp)
        )
    }
}

@ExperimentalCoilApi
@Composable
internal fun MoviePoster(modifier: Modifier = Modifier, imageUrl: String?) {
    imageUrl?.let {
        Image(
            painter = rememberImagePainter(
                data = UrlProvider.imageUrl + "/original/$imageUrl",
                builder = {
                    crossfade(true)
                }),
            contentDescription = null,
            modifier = modifier,
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
internal fun MovieTitleText(modifier: Modifier = Modifier, movieTitle: String) {
    Text(
        text = movieTitle,
        modifier = modifier,
        style = MaterialTheme.typography.h5,
        fontWeight = FontWeight.Bold,
    )
}

@ExperimentalUnitApi
@Composable
internal fun MovieOverviewText(modifier: Modifier = Modifier, overviewText: String) {
    Text(
        text = overviewText,
        modifier = modifier,
        style = MaterialTheme.typography.body1,
        letterSpacing = TextUnit(0.2f, TextUnitType.Sp)
    )
}

@Composable
internal fun TextWithMediumEmphasis(modifier: Modifier = Modifier, text: String, style: TextStyle = MaterialTheme.typography.body1) {
    Text(
        text = text,
        modifier = modifier,
        color = Grey500,
        style = style
    )
}