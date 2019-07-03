package com.uoooo.mvvm.example.ui.common

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.paging.DataSource

abstract class BaseDataSourceFactory<Key, Value> : DataSource.Factory<Key, Value>(), LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    abstract fun onCleared()
}
