package dev.sijanrijal.popularmovies.core.network.services.search

import com.google.gson.annotations.SerializedName

data class MovieSearchNetworkModel(
    @SerializedName("page") val pageNumber: Int,
    @SerializedName("results") val searchResults: List<MovieSearchNetwork>
)

data class MovieSearchNetwork(
    @SerializedName("poster_path") val posterPath: String?,
    val overview: String,
    @SerializedName("release_date") val releaseDate: String,
    @SerializedName("id") val movieID: Int,
    @SerializedName("title") val movieTitle: String,
    @SerializedName("vote_average") val rating: Float,
)