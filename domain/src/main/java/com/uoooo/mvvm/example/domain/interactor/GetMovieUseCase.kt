package com.uoooo.mvvm.example.domain.interactor

import com.uoooo.mvvm.example.domain.model.Movie
import io.reactivex.Single

interface GetMovieUseCase {
    fun getPopularMovie(page: Int): Single<List<Movie>>
}
