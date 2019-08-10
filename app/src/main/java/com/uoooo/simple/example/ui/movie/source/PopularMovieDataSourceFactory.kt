package com.uoooo.simple.example.ui.movie.source

import androidx.paging.DataSource
import com.uoooo.simple.example.data.source.remote.MovieDataSourceRemote
import com.uoooo.simple.example.domain.model.Movie
import com.uoooo.simple.example.ui.common.BasePageKeyedDataSourceFactory

class PopularMovieDataSourceFactory(
    private val sourceRemote: MovieDataSourceRemote,
    override val startPage: Int,
    override val endPage: Int
) : BasePageKeyedDataSourceFactory<Int, Movie>(startPage, endPage) {
    override fun create(): DataSource<Int, Movie> {
        return PopularMovieDataSource(sourceRemote, startPage, endPage).apply {
            dataSource.accept(this)
        }
    }
}
