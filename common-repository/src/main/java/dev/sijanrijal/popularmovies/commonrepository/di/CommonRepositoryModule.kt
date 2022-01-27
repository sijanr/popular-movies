package dev.sijanrijal.popularmovies.commonrepository.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dev.sijanrijal.popularmovies.commonrepository.genre.MovieGenreRepository
import dev.sijanrijal.popularmovies.commonrepository.genre.MovieGenreRepositoryImpl

@Module
@InstallIn(AppScope::class)
abstract class CommonRepositoryModule {
    @Binds
    abstract fun genreRepository(genreRepositoryImpl: MovieGenreRepositoryImpl): MovieGenreRepository
}