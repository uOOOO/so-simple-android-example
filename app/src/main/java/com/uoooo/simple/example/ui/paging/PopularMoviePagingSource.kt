package com.uoooo.simple.example.ui.paging

import androidx.paging.rxjava3.RxPagingSource
import com.uoooo.simple.example.domain.interactor.MovieUseCase
import com.uoooo.simple.example.domain.model.Movie
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

class PopularMoviePagingSource constructor(
    private val startPage: Int,
    private val endPage: Int,
    private val movieUseCase: MovieUseCase
) : RxPagingSource<Int, Movie>() {
    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, Movie>> {
        val page = params.key ?: startPage
        return movieUseCase.getPopularMovie(page)
            .map { toLoadResult(it, page) }
            .onErrorReturn { LoadResult.Error(it) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    private fun toLoadResult(
        users: List<Movie>,
        page: Int
    ): LoadResult<Int, Movie> {
        return LoadResult.Page(
            users,
            null,
            (page + 1).takeIf { users.isNotEmpty() && it <= endPage },
            LoadResult.Page.COUNT_UNDEFINED,
            LoadResult.Page.COUNT_UNDEFINED
        )
    }
}