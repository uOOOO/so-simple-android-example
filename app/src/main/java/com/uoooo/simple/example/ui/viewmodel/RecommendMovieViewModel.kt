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
import com.uoooo.simple.example.repo.RecommendMovieRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Observable
import io.sellmair.disposer.disposeBy

class RecommendMovieViewModel @ViewModelInject constructor(
    @ApplicationContext context: Context,
    private val repository: RecommendMovieRepository
) : BaseViewModel(context as Application) {
    val recommendMovieList: Observable<PagingData<Movie>> =
        BehaviorRelay.create<PagingData<Movie>>().toSerialized().apply {
            hide()
        }

    private val loadRecommendMovie = BehaviorRelay.create<Triple<Int, Int, Int>>().toSerialized()
    private val invalidate = PublishRelay.create<Unit>().toSerialized()

    init {
        loadRecommendMovie.toFlowable(BackpressureStrategy.LATEST)
            .switchMap {
                repository
                    .getRecommendMovie(it.first, it.second, it.third)
            }
            .cachedIn(viewModelScope)
            .subscribe(recommendMovieList.asSerializedRelay())
            .disposeBy(disposer)

        invalidate
            .subscribe { repository.invalidate() }
            .disposeBy(disposer)
    }

    fun loadRecommendMovie(id: Int, startPage: Int, endPage: Int) {
        loadRecommendMovie.accept(Triple(id, startPage, endPage))
    }

    fun invalidateRecommendMovie() {
        invalidate.accept(Unit)
    }
}
