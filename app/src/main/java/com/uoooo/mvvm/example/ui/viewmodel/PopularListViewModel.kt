package com.uoooo.mvvm.example.ui.viewmodel

import android.app.Application
import androidx.paging.PagedList
import androidx.paging.RxPagedListBuilder
import com.uoooo.mvvm.example.domain.model.Movie
import com.uoooo.mvvm.example.domain.repository.MovieRepository
import com.uoooo.mvvm.example.ui.common.BasePagingViewModel
import com.uoooo.mvvm.example.ui.movie.source.PopularDataSourceFactory
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject

class PopularListViewModel(application: Application, private val repository: MovieRepository) :
    BasePagingViewModel<Movie>(application) {
    private lateinit var pagedList: Subject<PagedList<Movie>>

    fun getPopularList(startPage: Int, endPage: Int): Observable<PagedList<Movie>> {
        if (!::pagedList.isInitialized) {
            pagedList = createPagedPopularList(startPage, endPage)
                .subscribeWith(BehaviorSubject.create())
        }
        return pagedList
    }

    private fun createPagedPopularList(startPage: Int, endPage: Int): Observable<PagedList<Movie>> {
        val factory = PopularDataSourceFactory(repository, startPage, endPage, _networkState)
            .apply { factoryList += this }
        val config = PagedList.Config.Builder()
            .setPageSize(10)
            .setEnablePlaceholders(false)
            .build()
        return RxPagedListBuilder(factory, config)
            .buildObservable()
    }
}
