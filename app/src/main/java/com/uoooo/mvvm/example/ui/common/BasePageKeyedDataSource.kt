package com.uoooo.mvvm.example.ui.common

import androidx.annotation.CallSuper
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.paging.PageKeyedDataSource
import io.reactivex.disposables.CompositeDisposable

abstract class BasePageKeyedDataSource<Key, Value> : PageKeyedDataSource<Key, Value>(), LifecycleObserver {
    @Suppress("MemberVisibilityCanBePrivate")
    protected val disposer by lazy {
        CompositeDisposable()
    }

    @CallSuper
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onCleared() {
        if (!disposer.isDisposed) {
            disposer.dispose()
        }
    }
}
