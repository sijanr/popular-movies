package dev.sijanrijal.popularmovies.core.network.services.popular

import com.google.gson.annotations.SerializedName

data class PopularMoviesResponse(
    @SerializedName("pages") val pageNumber: Int = 1,
    @SerializedName("results") val movies: List<PopularMovies>
)

data class PopularMovies(
    @SerializedName("poster_path") val posterUrl: String?,
    @SerializedName("backdrop_path") val backdropUrl: String,
    @SerializedName("overview") val overview: String,
    @SerializedName("release_date") val releaseDate: String,
    @SerializedName("title") val title: String,
    @SerializedName("vote_average") val rating: Float,
)