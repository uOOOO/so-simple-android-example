package com.uoooo.simple.example.ui.common

import androidx.paging.PageKeyedDataSource
import com.jakewharton.rxrelay2.PublishRelay
import com.uoooo.simple.example.ui.movie.repository.model.LoadingState
import io.reactivex.Observable

abstract class BasePageKeyedDataSource<Key, Value>(
    open val startPage: Int,
    open val endPage: Int
) : PageKeyedDataSource<Key, Value>() {
    protected val loadingState: PublishRelay<LoadingState> by lazy {
        PublishRelay.create<LoadingState>()
    }

    fun getLoadingState(): Observable<LoadingState> {
        return loadingState.hide()
    }
}
