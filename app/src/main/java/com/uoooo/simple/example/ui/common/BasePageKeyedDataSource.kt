package com.uoooo.simple.example.ui.common

import androidx.annotation.CallSuper
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.paging.PageKeyedDataSource
import com.uoooo.simple.example.ui.viewmodel.state.PagingState
import io.reactivex.subjects.Subject
import io.sellmair.disposer.Disposer

abstract class BasePageKeyedDataSource<Value>(
    open val startPage: Int,
    open val endPage: Int,
    open val pagingState: Subject<PagingState>
) : PageKeyedDataSource<Int, Value>(), LifecycleObserver {
    @Suppress("MemberVisibilityCanBePrivate")
    protected val disposer by lazy {
        Disposer.create()
    }

    @CallSuper
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onCleared() {
        if (!disposer.isDisposed) {
            disposer.dispose()
        }
    }
}
