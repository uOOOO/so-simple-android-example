package com.uoooo.mvvm.example.ui.movie.source

import androidx.paging.DataSource
import com.uoooo.mvvm.example.domain.model.Movie
import com.uoooo.mvvm.example.domain.repository.MovieRepository
import com.uoooo.mvvm.example.ui.common.BaseDataSourceFactory
import io.reactivex.Observable

class RecommendationMovieDataSourceFactory(
    private val repository: MovieRepository,
    private val id: Int,
    override val startPage: Int,
    override val endPage: Int
) : BaseDataSourceFactory<Movie>(startPage, endPage) {
    private lateinit var popularMovieDataSource: RecommendationsMovieDataSource

    override fun create(): DataSource<Int, Movie> {
        popularMovieDataSource = RecommendationsMovieDataSource(repository, id, startPage, endPage)
        return popularMovieDataSource
    }

    override fun onCleared() {
        if (::popularMovieDataSource.isInitialized) popularMovieDataSource.onCleared()
    }
}
