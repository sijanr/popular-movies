package dev.sijanrijal.popularmovies.screens.search.model

data class MovieSearch(
    val posterPath: String?,
    val overview: String,
    val releaseDate: String,
    val movieID: Int,
    val movieTitle: String,
    val rating: Float,
)