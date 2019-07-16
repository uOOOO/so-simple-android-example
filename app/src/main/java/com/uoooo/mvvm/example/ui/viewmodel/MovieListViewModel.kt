package com.uoooo.mvvm.example.ui.viewmodel

import android.app.Application
import androidx.paging.PagedList
import androidx.paging.RxPagedListBuilder
import com.uoooo.mvvm.example.domain.model.Movie
import com.uoooo.mvvm.example.domain.repository.MovieRepository
import com.uoooo.mvvm.example.ui.common.BaseDataSourceFactory
import com.uoooo.mvvm.example.ui.common.BaseViewModel
import com.uoooo.mvvm.example.ui.movie.source.PopularDataSourceFactory
import com.uoooo.mvvm.example.ui.movie.source.RecommendationMovieDataSourceFactory
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable

// TODO : need view state - loading, error...
class MovieListViewModel(application: Application, private val repository: MovieRepository) :
    BaseViewModel(application) {
    private val factoryList by lazy {
        mutableListOf<BaseDataSourceFactory<*>>()
    }

    fun getPopularList(): Flowable<PagedList<Movie>> {
        val factory = PopularDataSourceFactory(repository, 1, 5)
        factoryList += factory
        val config = PagedList.Config.Builder()
            .setPageSize(10)
            .setEnablePlaceholders(false)
            .build()
        return RxPagedListBuilder(factory, config)
            .buildFlowable(BackpressureStrategy.LATEST)
    }

    fun getRecommendationList(id: Int): Flowable<PagedList<Movie>> {
        val factory = RecommendationMovieDataSourceFactory(repository, id, 1, 1)
        factoryList += factory
        val config = PagedList.Config.Builder()
            .setPageSize(10)
            .setEnablePlaceholders(false)
            .build()
        return RxPagedListBuilder(factory, config)
            .buildFlowable(BackpressureStrategy.LATEST)
    }

    override fun onCleared() {
        super.onCleared()
        factoryList.forEach { it.onCleared() }
    }
}
