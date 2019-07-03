package com.uoooo.mvvm.example.ui.movie.source

import androidx.paging.DataSource
import com.uoooo.mvvm.example.ui.common.BaseDataSourceFactory
import com.uoooo.mvvm.example.domain.model.Movie
import com.uoooo.mvvm.example.domain.repository.MovieRepository

class PopularMovieDataSourceFactory(private val repository: MovieRepository) :
    BaseDataSourceFactory<Int, Movie>() {
    private lateinit var popularMovieDataSource: PopularMovieDataSource

    override fun create(): DataSource<Int, Movie> {
        popularMovieDataSource = PopularMovieDataSource(repository)
        return popularMovieDataSource
    }

    override fun onCleared() {
        popularMovieDataSource.onCleared()
    }
}
