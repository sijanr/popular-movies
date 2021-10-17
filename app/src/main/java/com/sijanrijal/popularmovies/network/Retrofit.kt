package com.sijanrijal.popularmovies.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

private val baseUrl = "https://api.themoviedb.org/3/"
val imageUrl = "https://image.tmdb.org/t/p/"

private val logging by lazy {
    HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
}

private val okHttpClient by lazy {
    OkHttpClient.Builder().addInterceptor(logging).build()
}

private val retrofit by lazy {
    Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}

val moviesApi by lazy {
    retrofit.create(MovieApiService::class.java)
}