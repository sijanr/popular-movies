package dev.sijanrijal.popularmovies.home.network

data class UrlProvider(
    val baseUrl: String = "https://api.themoviedb.org/3/",
    val imageUrl: String = "https://image.tmdb.org/t/p/"
)