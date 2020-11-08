package com.uoooo.simple.example.ui.common

import android.app.Application
import androidx.annotation.CallSuper
import androidx.lifecycle.AndroidViewModel
import com.jakewharton.rxrelay3.Relay
import io.reactivex.rxjava3.core.Observable
import io.sellmair.disposer.Disposer

open class BaseViewModel(application: Application) : AndroidViewModel(application) {
    protected val disposer by lazy {
        Disposer.create()
    }

    @CallSuper
    override fun onCleared() {
        super.onCleared()
        disposer.dispose()
    }

    protected fun <T> Observable<T>.asSerializedRelay() = this as Relay<T>
}
