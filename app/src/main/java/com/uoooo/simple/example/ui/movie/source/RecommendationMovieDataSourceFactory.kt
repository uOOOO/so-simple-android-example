package com.uoooo.simple.example.ui.movie.source

import androidx.paging.DataSource
import com.uoooo.simple.example.domain.model.Movie
import com.uoooo.simple.example.domain.repository.MovieRepository
import com.uoooo.simple.example.ui.common.BaseDataSourceFactory
import com.uoooo.simple.example.ui.viewmodel.state.PagingState
import io.reactivex.subjects.Subject

class RecommendationMovieDataSourceFactory(
    private val repository: MovieRepository,
    private val id: Int,
    override val startPage: Int,
    override val endPage: Int,
    override val pagingState: Subject<PagingState>
) : BaseDataSourceFactory<Movie>(startPage, endPage, pagingState) {
    private lateinit var popularMovieDataSource: RecommendationsMovieDataSource

    override fun create(): DataSource<Int, Movie> {
        popularMovieDataSource = RecommendationsMovieDataSource(repository, id, startPage, endPage, pagingState)
        return popularMovieDataSource
    }

    override fun invalidate() {
        if (::popularMovieDataSource.isInitialized) popularMovieDataSource.invalidate()
    }

    override fun onCleared() {
        if (::popularMovieDataSource.isInitialized) popularMovieDataSource.onCleared()
    }
}
