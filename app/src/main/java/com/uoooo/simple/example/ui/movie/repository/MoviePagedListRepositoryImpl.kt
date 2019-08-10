package com.uoooo.simple.example.ui.movie.repository

import androidx.paging.PagedList
import androidx.paging.RxPagedListBuilder
import com.uoooo.simple.example.data.source.remote.MovieDataSourceRemote
import com.uoooo.simple.example.domain.model.Movie
import com.uoooo.simple.example.ui.movie.repository.model.PagedListState
import com.uoooo.simple.example.ui.movie.source.PopularMovieDataSourceFactory
import com.uoooo.simple.example.ui.movie.source.RecommendMovieDataSourceFactory

class MoviePagedListRepositoryImpl(
    private val sourceRemote: MovieDataSourceRemote
) : MoviePagedListRepository {
    override fun getPopular(startPage: Int, endPage: Int): PagedListState<Movie> {
        val factory = PopularMovieDataSourceFactory(sourceRemote, startPage, endPage)
        val config = PagedList.Config.Builder()
            .setPageSize(10)
            .setEnablePlaceholders(false)
            .build()
        return PagedListState(
            RxPagedListBuilder(factory, config).buildObservable(),
            factory.getDataSource().switchMap { it.getLoadingState() },
            invalidate = {
                factory.getDataSource().blockingFirst().invalidate()
            }
        )
    }

    override fun getRecommend(id: Int, startPage: Int, endPage: Int): PagedListState<Movie> {
        val factory = RecommendMovieDataSourceFactory(sourceRemote, id, startPage, endPage)
        val config = PagedList.Config.Builder()
            .setPageSize(10)
            .setEnablePlaceholders(true)
            .build()
        return PagedListState(
            RxPagedListBuilder(factory, config).buildObservable(),
            factory.getDataSource().switchMap { it.getLoadingState() },
            invalidate = {
                factory.getDataSource().blockingFirst().invalidate()
            }
        )
    }
}
