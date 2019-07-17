package com.uoooo.simple.example.ui.movie.source

import com.uoooo.simple.example.domain.model.Movie
import com.uoooo.simple.example.domain.repository.MovieRepository
import com.uoooo.simple.example.extension.printEnhancedStackTrace
import com.uoooo.simple.example.ui.common.BasePageKeyedDataSource
import com.uoooo.simple.example.ui.viewmodel.state.PagingState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.Subject
import io.sellmair.disposer.disposeBy

class RecommendationsMovieDataSource(
    private val repository: MovieRepository,
    private val id: Int,
    override val startPage: Int,
    override val endPage: Int,
    override val pagingState: Subject<PagingState>
) : BasePageKeyedDataSource<Movie>(startPage, endPage, pagingState) {
    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Movie>
    ) {
        pagingState.onNext(PagingState.InitialLoading)
        repository.getRecommendations(id, startPage)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                callback.onResult(it, null, startPage + 1)
                pagingState.onNext(PagingState.Loaded)
            }, {
                it.printEnhancedStackTrace()
                pagingState.onNext(PagingState.Error(it))
            })
            .disposeBy(disposer)
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
        if (endPage < params.key) {
            return
        }
        pagingState.onNext(PagingState.Loading)
        repository.getRecommendations(id, params.key)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                callback.onResult(it, params.key + 1)
                pagingState.onNext(PagingState.Loaded)
            }, {
                it.printEnhancedStackTrace()
                pagingState.onNext(PagingState.Error(it))
            })
            .disposeBy(disposer)
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {

    }
}
