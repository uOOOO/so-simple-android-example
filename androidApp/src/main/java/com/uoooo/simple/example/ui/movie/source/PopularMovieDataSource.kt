package com.uoooo.simple.example.ui.movie.source

import android.annotation.SuppressLint
import com.uoooo.simple.example.data.mapper.mapToModel
import com.uoooo.simple.example.data.source.remote.MovieDataSourceRemote
import com.uoooo.simple.example.domain.model.Movie
import com.uoooo.simple.example.extension.printEnhancedStackTrace
import com.uoooo.simple.example.ui.common.BasePageKeyedDataSource
import com.uoooo.simple.example.ui.movie.repository.model.LoadingState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class PopularMovieDataSource(
    private val sourceRemote: MovieDataSourceRemote,
    override val startPage: Int,
    override val endPage: Int
) : BasePageKeyedDataSource<Int, Movie>(startPage, endPage) {
    @SuppressLint("CheckResult")
    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Movie>
    ) {
        sourceRemote.getPopularMovie(startPage)
            .doOnSubscribe { loadingState.accept(LoadingState.InitialLoading) }
            .map { it ->
                it.asSequence()
                    .map { it.mapToModel() }
                    .filter { it != null }
                    .map { it!! }
                    .toList()
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                callback.onResult(it, null, startPage + 1)
                loadingState.accept(LoadingState.Loaded(it.isEmpty()))
            }, {
                it.printEnhancedStackTrace()
                loadingState.accept(LoadingState.Error(it, LoadingState.InitialLoading))
            })
    }

    @SuppressLint("CheckResult")
    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
        if (endPage < params.key) {
            return
        }
        sourceRemote.getPopularMovie(params.key)
            .doOnSubscribe { loadingState.accept(LoadingState.Loading) }
            .map { it ->
                it.asSequence()
                    .map { it.mapToModel() }
                    .filter { it != null }
                    .map { it!! }
                    .toList()
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                callback.onResult(it, params.key + 1)
                loadingState.accept(LoadingState.Loaded(it.isEmpty()))
            }, {
                it.printEnhancedStackTrace()
                loadingState.accept(LoadingState.Error(it, LoadingState.Loading))
            })
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {

    }
}
