package dev.sijanrijal.popularmovies.core.network.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.sijanrijal.popularmovies.core.network.UrlProvider
import dev.sijanrijal.popularmovies.core.network.services.moviedetail.MovieDetailApiService
import dev.sijanrijal.popularmovies.core.network.services.nowplaying.NowPlayingApiService
import dev.sijanrijal.popularmovies.core.network.services.popular.PopularApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @AppScope
    fun urlProvider() = UrlProvider

    @Provides
    @AppScope
    fun httpLoggingInterceptor() = HttpLoggingInterceptor()
        .setLevel(HttpLoggingInterceptor.Level.BODY)

    @Provides
    @AppScope
    fun okHtpClient(httpLoggingInterceptor: HttpLoggingInterceptor) = OkHttpClient.Builder()
        .addInterceptor(httpLoggingInterceptor).build()

    @Provides
    @AppScope
    fun retrofit(urlProvider: UrlProvider, okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(urlProvider.baseUrl)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @AppScope
    fun nowPlayingApiService(retrofit: Retrofit): NowPlayingApiService = retrofit.create(NowPlayingApiService::class.java)

    @Provides
    @AppScope
    fun popularMoviesService(retrofit: Retrofit): PopularApiService = retrofit.create(PopularApiService::class.java)

    @Provides
    @AppScope
    fun movieDetailService(retrofit: Retrofit): MovieDetailApiService = retrofit.create(MovieDetailApiService::class.java)
}