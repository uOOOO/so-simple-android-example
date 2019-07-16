package com.uoooo.mvvm.example.ui.movie.source

import com.uoooo.mvvm.example.ui.common.BasePageKeyedDataSource
import com.uoooo.mvvm.example.domain.model.Movie
import com.uoooo.mvvm.example.domain.repository.MovieRepository
import com.uoooo.mvvm.example.extension.printEnhancedStackTrace
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.sellmair.disposer.disposeBy

class RecommendationsMovieDataSource(
    private val repository: MovieRepository,
    private val id: Int,
    override val startPage: Int,
    override val endPage: Int
) : BasePageKeyedDataSource<Movie>(startPage, endPage) {
    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Movie>
    ) {
        repository.getRecommendations(id, startPage)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                callback.onResult(it, null, startPage + 1)
            }, {
                it.printEnhancedStackTrace()
            })
            .disposeBy(disposer)
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
        if (endPage < params.key) {
            return
        }
        repository.getRecommendations(id, params.key)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                callback.onResult(it, params.key + 1)
            }, {
                it.printEnhancedStackTrace()
            })
            .disposeBy(disposer)
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {

    }
}
