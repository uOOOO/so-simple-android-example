package com.uoooo.simple.example.repo

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.rxjava3.flowable
import com.uoooo.simple.example.domain.interactor.MovieUseCase
import com.uoooo.simple.example.domain.model.Movie
import io.reactivex.rxjava3.core.Flowable
import javax.inject.Inject

class PopularMovieRepository @Inject constructor(private val movieUseCase: MovieUseCase) {
    private var pagingSource: PopularMoviePagingSource? = null

    fun getPopularMovie(startPage: Int, endPage: Int): Flowable<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(pageSize = 21, enablePlaceholders = false),
            pagingSourceFactory = {
                PopularMoviePagingSource(
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
