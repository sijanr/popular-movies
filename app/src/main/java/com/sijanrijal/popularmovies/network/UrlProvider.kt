package com.sijanrijal.popularmovies.network

data class UrlProvider(
    val baseUrl: String = "https://api.themoviedb.org/3/",
    val imageUrl: String = "https://image.tmdb.org/t/p/"
)