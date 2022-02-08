package dev.sijanrijal.popularmovies.screens.search.model

data class UpcomingMovies(
    val id: Long,
    val posterUrl: String?,
    val overview: String,
    val releaseDate: String,
    val movieTitle: String,
    val rating: Float,
)