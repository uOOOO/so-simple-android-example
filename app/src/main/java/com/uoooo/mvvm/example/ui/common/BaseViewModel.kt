package com.uoooo.mvvm.example.ui.common

import android.app.Application
import androidx.annotation.CallSuper
import androidx.lifecycle.AndroidViewModel
import io.reactivex.disposables.CompositeDisposable

open class BaseViewModel(application: Application) : AndroidViewModel(application) {
    @Suppress("MemberVisibilityCanBePrivate")
    protected val disposer by lazy {
        CompositeDisposable()
    }

    @CallSuper
    override fun onCleared() {
        super.onCleared()
        disposer.dispose()
    }
}
