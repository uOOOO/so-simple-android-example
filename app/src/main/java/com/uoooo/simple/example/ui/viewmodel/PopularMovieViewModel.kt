package com.uoooo.simple.example.ui.viewmodel

import android.app.Application
import com.jakewharton.rxrelay2.BehaviorRelay
import com.uoooo.simple.example.domain.model.Movie
import com.uoooo.simple.example.ui.common.BasePagingViewModel
import com.uoooo.simple.example.ui.movie.repository.MoviePagedListRepository

class PopularMovieViewModel(application: Application, private val repository: MoviePagedListRepository) :
    BasePagingViewModel<Movie>(application) {
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
