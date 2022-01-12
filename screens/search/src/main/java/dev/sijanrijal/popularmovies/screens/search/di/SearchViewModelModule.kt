package dev.sijanrijal.popularmovies.screens.search.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dev.sijanrijal.popularmovies.screens.search.repository.SearchRepository
import dev.sijanrijal.popularmovies.screens.search.repository.SearchRepositoryImpl

@Module
@InstallIn(ViewModelComponent::class)
abstract class SearchViewModelModule {
    @Binds
    abstract fun searchRepository(searchRepositoryImpl: SearchRepositoryImpl): SearchRepository
}