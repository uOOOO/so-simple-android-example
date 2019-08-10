package com.uoooo.simple.example.ui.common

import android.app.Application
import androidx.paging.PagedList
import com.jakewharton.rxrelay2.BehaviorRelay
import com.uoooo.simple.example.ui.movie.repository.model.LoadingState
import com.uoooo.simple.example.ui.movie.repository.model.PagedListState
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

open class BasePagingViewModel<T>(application: Application) : BaseViewModel(application) {
    protected val result: BehaviorRelay<PagedListState<T>> by lazy {
        BehaviorRelay.create<PagedListState<T>>().apply {
            switchMap {
                it.loadingState.subscribe(loadingState)
                return@switchMap it.pagedList
            }.subscribe(pagedList)
        }
    }

    protected val loadingState: PublishSubject<LoadingState> by lazy {
        PublishSubject.create<LoadingState>()
    }

    protected val pagedList: BehaviorSubject<PagedList<T>> by lazy {
        BehaviorSubject.create<PagedList<T>>()
    }

    fun getLoadingState(): Observable<LoadingState> {
        return loadingState.hide()
    }

    fun getPagedList(): Observable<PagedList<T>> {
        return pagedList.hide()
    }
    
    fun invalidate() {
        result.value?.invalidate?.invoke()
    }
}
