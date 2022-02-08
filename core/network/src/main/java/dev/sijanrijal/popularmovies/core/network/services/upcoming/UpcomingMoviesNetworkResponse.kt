package dev.sijanrijal.popularmovies.core.network.services.upcoming

import com.google.gson.annotations.SerializedName

data class UpcomingMoviesNetworkResponse(
    @SerializedName("page") val page: Int,
    @SerializedName("results") val upcomingMovies: List<UpcomingMoviesNetwork>
)

data class UpcomingMoviesNetwork(
    @SerializedName("poster_path") val posterUrl: String?,
    @SerializedName("overview") val overview: String,
    @SerializedName("release_date") val releaseDate: String,
    @SerializedName("title") val movieTitle: String,
    @SerializedName("vote_average") val rating: Float,
    @SerializedName("id") val id: Long,
)