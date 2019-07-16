package com.uoooo.mvvm.example.ui.common

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.paging.DataSource

abstract class BaseDataSourceFactory<Value>(
    open val startPage: Int,
    open val endPage: Int
) : DataSource.Factory<Int, Value>(), LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    abstract fun onCleared()
}
