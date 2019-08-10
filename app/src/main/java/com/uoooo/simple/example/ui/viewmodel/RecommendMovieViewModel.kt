package com.uoooo.simple.example.ui.viewmodel

import android.app.Application
import com.jakewharton.rxrelay2.BehaviorRelay
import com.uoooo.simple.example.domain.model.Movie
import com.uoooo.simple.example.ui.common.BasePagingViewModel
import com.uoooo.simple.example.ui.movie.repository.MoviePagedListRepository

class RecommendMovieViewModel(application: Application, private val repository: MoviePagedListRepository) :
    BasePagingViewModel<Movie>(application) {
    private val request: BehaviorRelay<RecommendationList> by lazy {
        BehaviorRelay.create<RecommendationList>().apply {
            distinctUntilChanged()
                .map { repository.getRecommend(it.id, it.startPage, it.endPage) }
                .subscribe(result)
        }
    }

    fun loadRecommendationList(id: Int, startPage: Int, endPage: Int) {
        request.accept(RecommendationList(id, startPage, endPage))
    }

    private data class RecommendationList(
        val id: Int,
        val startPage: Int,
        val endPage: Int
    )
}
