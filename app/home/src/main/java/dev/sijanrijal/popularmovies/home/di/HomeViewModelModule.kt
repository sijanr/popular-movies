package dev.sijanrijal.popularmovies.home.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dev.sijanrijal.popularmovies.home.repository.MovieRepository
import dev.sijanrijal.popularmovies.home.repository.MovieRepositoryImpl

@Module
@InstallIn(ViewModelComponent::class)
abstract class HomeViewModelModule {

    @Binds
    abstract fun movieRepository(movieRepositoryImpl: MovieRepositoryImpl) : MovieRepository
}