package com.uoooo.mvvm.example.ui.viewmodel

import android.app.Application
import androidx.paging.PagedList
import androidx.paging.RxPagedListBuilder
import com.uoooo.mvvm.example.domain.model.Movie
import com.uoooo.mvvm.example.domain.repository.MovieRepository
import com.uoooo.mvvm.example.ui.common.BaseViewModel
import com.uoooo.mvvm.example.ui.movie.source.PopularMovieDataSourceFactory
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable

// TODO : need view state - loading, error...
class MovieListViewModel(application: Application, private val repository: MovieRepository) :
    BaseViewModel(application) {
    // TODO : need DI?
    private val popularMovieDataSourceFactory by lazy {
        PopularMovieDataSourceFactory(repository)
    }

    fun getPopularMovieList(): Flowable<PagedList<Movie>> {
        val config = PagedList.Config.Builder()
            .setPageSize(10)
            .setEnablePlaceholders(false)
            .build()
        return RxPagedListBuilder(popularMovieDataSourceFactory, config)
            .buildFlowable(BackpressureStrategy.LATEST)
    }

    override fun onCleared() {
        super.onCleared()
        popularMovieDataSourceFactory.onCleared()
    }
}
