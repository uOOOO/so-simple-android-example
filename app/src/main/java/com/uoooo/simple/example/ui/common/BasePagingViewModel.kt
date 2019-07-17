package com.uoooo.simple.example.ui.common

import android.app.Application
import androidx.annotation.CallSuper
import androidx.paging.PagedListAdapter
import com.uoooo.simple.example.ui.viewmodel.state.PagingState
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject

open class BasePagingViewModel<T>(application: Application) : BaseViewModel(application) {
    @Suppress("PropertyName")
    protected val _networkState by lazy {
        PublishSubject.create<PagingState>()
    }

    val networkState: Observable<PagingState> by lazy {
        _networkState.hide()
            .observeOn(AndroidSchedulers.mainThread())
    }

    protected val factoryList by lazy {
        mutableListOf<BaseDataSourceFactory<T>>()
    }

    fun invalidate(adapter: PagedListAdapter<*, *>) {
        adapter.currentList?.dataSource?.invalidate()
    }

    @CallSuper
    override fun onCleared() {
        super.onCleared()
        factoryList.forEach { it.onCleared() }
    }
}
