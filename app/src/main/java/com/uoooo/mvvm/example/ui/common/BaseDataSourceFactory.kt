package com.uoooo.mvvm.example.ui.common

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.paging.DataSource
import com.uoooo.mvvm.example.ui.viewmodel.state.PagingState
import io.reactivex.subjects.Subject

abstract class BaseDataSourceFactory<Value>(
    open val startPage: Int,
    open val endPage: Int,
    open val pagingState: Subject<PagingState>
) : DataSource.Factory<Int, Value>(), LifecycleObserver {

    abstract fun invalidate()

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    abstract fun onCleared()
}
