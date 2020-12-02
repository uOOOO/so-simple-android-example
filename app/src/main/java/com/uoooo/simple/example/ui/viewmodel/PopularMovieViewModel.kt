package com.uoooo.simple.example.ui.viewmodel

import android.app.Application
import android.content.Context
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.rxjava3.cachedIn
import com.jakewharton.rxrelay3.BehaviorRelay
import com.jakewharton.rxrelay3.PublishRelay
import com.uoooo.simple.example.domain.model.Movie
import com.uoooo.simple.example.ui.common.BaseViewModel
import com.uoooo.simple.example.ui.paging.PopularMovieRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Observable
import io.sellmair.disposer.disposeBy

class PopularMovieViewModel @ViewModelInject constructor(
    @ApplicationContext context: Context,
    private val repository: PopularMovieRepository
) : BaseViewModel(context as Application) {
    val popularMovieList: Observable<PagingData<Movie>> =
        BehaviorRelay.create<PagingData<Movie>>().toSerialized().apply {
            hide()
        }

    private val loadPopularMovie = BehaviorRelay.create<Pair<Int, Int>>().toSerialized()
    private val invalidate = PublishRelay.create<Unit>().toSerialized()

    init {
        loadPopularMovie.toFlowable(BackpressureStrategy.LATEST)
            .switchMap {
                repository
                    .getPopularMovie(it.first, it.second)
            }
            .cachedIn(viewModelScope)
            .subscribe(popularMovieList.asSerializedRelay())
            .disposeBy(disposer)

        invalidate
            .subscribe { repository.invalidate() }
            .disposeBy(disposer)
    }

    fun loadPopularMovie(startPage: Int, endPage: Int) {
        loadPopularMovie.accept(Pair(startPage, endPage))
    }

    fun invalidatePopularMovie() {
        invalidate.accept(Unit)
    }
}
