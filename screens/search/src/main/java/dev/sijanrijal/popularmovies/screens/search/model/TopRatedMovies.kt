package dev.sijanrijal.popularmovies.screens.search.model

import com.google.gson.annotations.SerializedName

data class TopRatedMovies(
    val id: Long,
    val posterUrl: String?,
    val overview: String,
    val releaseDate: String,
    val movieTitle: String,
    val rating: Float,
)