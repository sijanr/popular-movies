package dev.sijanrijal.popularmovies.core.network.services.genre

import com.google.gson.annotations.SerializedName

data class TMDBMovieGenreResponse(
    @SerializedName("genres") val genres: List<TMDBMovieGenre>
)

data class TMDBMovieGenre(
    @SerializedName("name") val genre: String,
    @SerializedName("id") val genreId: Long
)