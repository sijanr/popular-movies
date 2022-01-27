package dev.sijanrijal.popularmovies.core.network.services.moviedetail

import com.google.gson.annotations.SerializedName

data class MovieDetail(
    @SerializedName("backdrop_path") val backdropUrl: String?,
    @SerializedName("overview") val overview: String,
    @SerializedName("vote_average") val rating: Float,
    @SerializedName("poster_path") val posterUrl: String?,
    @SerializedName("release_date") val releaseDate: String,
    @SerializedName("runtime") val movieRuntime: Int?,
    @SerializedName("title") val title: String,
    @SerializedName("genres") val genres: List<Genres>
)

data class Genres(
    @SerializedName("id") val genreId: Long,
    @SerializedName("name") val genre: String
)