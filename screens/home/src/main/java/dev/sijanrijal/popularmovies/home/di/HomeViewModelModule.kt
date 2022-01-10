package dev.sijanrijal.popularmovies.home.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dev.sijanrijal.popularmovies.home.repository.HomeRepository
import dev.sijanrijal.popularmovies.home.repository.HomeRepositoryImpl

@Module
@InstallIn(ViewModelComponent::class)
abstract class HomeViewModelModule {

    @Binds
    abstract fun movieRepository(movieRepositoryImpl: HomeRepositoryImpl) : HomeRepository
}