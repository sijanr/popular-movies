package com.sijanrijal.popularmovies.di

import com.sijanrijal.popularmovies.repository.MovieRepository
import com.sijanrijal.popularmovies.repository.MovieRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class ViewModelModule {

    @Binds
    abstract fun movieRepository(movieRepositoryImpl: MovieRepositoryImpl) : MovieRepository
}