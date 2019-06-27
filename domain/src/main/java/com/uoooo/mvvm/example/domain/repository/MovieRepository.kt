package com.uoooo.mvvm.example.domain.repository

import com.uoooo.mvvm.example.domain.model.Movie
import io.reactivex.Single

interface MovieRepository {
    fun getPopularMovie(page: Int): Single<List<Movie>>
}
