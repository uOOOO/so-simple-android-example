package com.uoooo.simple.example.ui.paging

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.rxjava3.flowable
import com.uoooo.simple.example.domain.interactor.MovieUseCase
import com.uoooo.simple.example.domain.model.Movie
import io.reactivex.rxjava3.core.Flowable
import javax.inject.Inject

class RecommendMovieRepository @Inject constructor(private val movieUseCase: MovieUseCase) {
    private var pagingSource: RecommendMoviePagingSource? = null

    fun getRecommendMovie(id: Int, startPage: Int, endPage: Int): Flowable<PagingData<Movie>> {
        @Suppress("EXPERIMENTAL_API_USAGE")
        return Pager(
            config = PagingConfig(pageSize = 21, enablePlaceholders = false),
            pagingSourceFactory = {
                RecommendMoviePagingSource(
                    id,
                    startPage,
                    endPage,
                    movieUseCase
                ).also { pagingSource = it }
            }
        ).flowable
    }

    fun invalidate() {
        pagingSource?.invalidate()
    }
}
