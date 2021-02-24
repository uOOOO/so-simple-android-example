package com.uoooo.simple.example.data.di

import com.uoooo.simple.example.data.repository.MovieRepositoryImpl
import com.uoooo.simple.example.data.source.remote.MovieDataSourceRemote
import com.uoooo.simple.example.data.source.remote.api.MovieService
import com.uoooo.simple.example.domain.repository.MovieRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module(includes = [HttpModule::class])
@InstallIn(SingletonComponent::class)
object DataModule {
    @Provides
    fun provideMovieService(retrofit: Retrofit): MovieService =
        retrofit.create(MovieService::class.java)

    @Provides
    fun provideMovieRepository(movieDataSourceRemote: MovieDataSourceRemote): MovieRepository =
        MovieRepositoryImpl(movieDataSourceRemote)
}
