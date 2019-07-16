package com.uoooo.mvvm.example.ui.movie.source

import androidx.paging.DataSource
import com.uoooo.mvvm.example.domain.model.Movie
import com.uoooo.mvvm.example.domain.repository.MovieRepository
import com.uoooo.mvvm.example.ui.common.BaseDataSourceFactory

class PopularDataSourceFactory(
    private val repository: MovieRepository,
    override val startPage: Int,
    override val endPage: Int
) : BaseDataSourceFactory<Movie>(startPage, endPage) {
    private lateinit var popularDataSource: PopularDataSource

    override fun create(): DataSource<Int, Movie> {
        popularDataSource = PopularDataSource(repository, startPage, endPage)
        return popularDataSource
    }

    override fun onCleared() {
        if (::popularDataSource.isInitialized) popularDataSource.onCleared()
    }
}
