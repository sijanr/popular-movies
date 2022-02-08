package dev.sijanrijal.popularmovies.core.network.services.toprated

import com.google.gson.annotations.SerializedName

data class TopRatedMoviesNetworkResponse(
    @SerializedName("page") val page: Int,
    @SerializedName("results") val topRatedMovies: List<TopRatedMoviesNetwork>
)

data class TopRatedMoviesNetwork(
    @SerializedName("poster_path") val posterUrl: String?,
    @SerializedName("overview") val overview: String,
    @SerializedName("release_date") val releaseDate: String,
    @SerializedName("title") val movieTitle: String,
    @SerializedName("vote_average") val rating: Float,
    @SerializedName("id") val id: Long,
)