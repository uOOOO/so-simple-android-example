package com.uoooo.mvvm.example.ui.movie.source

import androidx.paging.DataSource
import com.uoooo.mvvm.example.domain.model.Movie
import com.uoooo.mvvm.example.domain.repository.MovieRepository
import com.uoooo.mvvm.example.ui.common.BaseDataSourceFactory

class PopularMovieDataSourceFactory(private val repository: MovieRepository) :
    BaseDataSourceFactory<Int, Movie>() {
    private lateinit var popularMovieDataSource: PopularMovieDataSource

    override fun create(): DataSource<Int, Movie> {
        popularMovieDataSource = PopularMovieDataSource(repository)
        return popularMovieDataSource
    }

    override fun onCleared() {
        if (::popularMovieDataSource.isInitialized) popularMovieDataSource.onCleared()
    }
}
