package com.uoooo.simple.example.ui.common

import androidx.paging.DataSource
import com.jakewharton.rxrelay2.BehaviorRelay
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

abstract class BasePageKeyedDataSourceFactory<Key, Value>(
    open val startPage: Int,
    open val endPage: Int
) : DataSource.Factory<Key, Value>() {
    protected val dataSource: BehaviorRelay<BasePageKeyedDataSource<Key, Value>> by lazy {
        BehaviorRelay.create<BasePageKeyedDataSource<Key, Value>>()
    }

    fun getDataSource(): Observable<BasePageKeyedDataSource<Key, Value>> {
        return dataSource.hide()
    }
}
