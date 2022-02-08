package dev.sijanrijal.popularmovies.screens.moviedetail.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dev.sijanrijal.popularmovies.screens.moviedetail.repository.MovieDetailRepository
import dev.sijanrijal.popularmovies.screens.moviedetail.repository.MovieDetailRepositoryImpl

@Module
@InstallIn(ViewModelComponent::class)
abstract class ViewModelModule {
    @Binds
    abstract fun movieDetailRepository(movieDetailRepositoryImpl: MovieDetailRepositoryImpl): MovieDetailRepository
}