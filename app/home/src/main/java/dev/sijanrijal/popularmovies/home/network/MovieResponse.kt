package dev.sijanrijal.popularmovies.home.network

import com.google.gson.annotations.SerializedName

data class MovieResponse(
    @SerializedName(value = "results")
    val movies: List<Movie?>,
)

data class Movie(
    @SerializedName(value = "backdrop_path")
    val movieBackDropImagePath: String?,
    @SerializedName(value = "genre_ids")
    val genre: List<Int>?,
    @SerializedName(value = "id")
    val id: Long?,
    @SerializedName(value = "original_title")
    val title: String?,
    @SerializedName(value = "overview")
    val overview: String?,
    @SerializedName(value = "poster_path")
    val posterUrl: String?,
    @SerializedName(value = "release_date")
    val releaseDate: String?,
    @SerializedName(value = "vote_average")
    val rating: Float?
)

sealed class Result {
    class Success(val result: MovieResponse): Result()
    object Failure: Result()
}