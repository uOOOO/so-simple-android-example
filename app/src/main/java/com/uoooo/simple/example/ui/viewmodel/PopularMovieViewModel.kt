package com.uoooo.simple.example.ui.viewmodel

import android.app.Application
import android.content.Context
import androidx.hilt.lifecycle.ViewModelInject
import com.jakewharton.rxrelay2.BehaviorRelay
import com.uoooo.simple.example.domain.model.Movie
import com.uoooo.simple.example.ui.common.BasePagingViewModel
import com.uoooo.simple.example.ui.movie.repository.MoviePagedListRepository
import dagger.hilt.android.qualifiers.ApplicationContext

class PopularMovieViewModel @ViewModelInject constructor(
    @ApplicationContext context: Context,
    private val repository: MoviePagedListRepository
) : BasePagingViewModel<Movie>(context as Application) {
    private val request: BehaviorRelay<PopularListRequest> by lazy {
        BehaviorRelay.create<PopularListRequest>().apply {
            distinctUntilChanged()
                .map { repository.getPopular(it.startPage, it.endPage) }
                .subscribe(result)
        }
    }

    fun loadPopularMovie(startPage: Int, endPage: Int) {
        request.accept(PopularListRequest(startPage, endPage))
    }

    private data class PopularListRequest(
        val startPage: Int,
        val endPage: Int
    )
}
