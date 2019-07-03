package com.uoooo.mvvm.example.ui.movie.source

import com.uoooo.mvvm.example.ui.common.BasePageKeyedDataSource
import com.uoooo.mvvm.example.domain.model.Movie
import com.uoooo.mvvm.example.domain.repository.MovieRepository
import com.uoooo.mvvm.example.extension.disposeBy
import com.uoooo.mvvm.example.extension.printEnhancedStackTrace
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class PopularMovieDataSource(private val repository: MovieRepository) : BasePageKeyedDataSource<Int, Movie>() {

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Movie>
    ) {
        repository.getPopularMovie(START_PAGE)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                callback.onResult(it, null, START_PAGE + 1)
            }, {
                it.printEnhancedStackTrace()
            })
            .disposeBy(disposer)
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
        repository.getPopularMovie(params.key)
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

    companion object {
        private const val START_PAGE = 1
    }
}
