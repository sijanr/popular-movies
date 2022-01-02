package dev.sijanrijal.popularmovies.home.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.sijanrijal.popularmovies.home.network.MovieApiService
import dev.sijanrijal.popularmovies.home.network.UrlProvider
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @AppScope
    fun urlProvider() = UrlProvider()

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
    fun retrofit(urlProvider: UrlProvider, okHttpClient: OkHttpClient) = Retrofit.Builder()
        .baseUrl(urlProvider.baseUrl)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @AppScope
    fun moviesApiService(retrofit: Retrofit) = retrofit.create(MovieApiService::class.java)

}