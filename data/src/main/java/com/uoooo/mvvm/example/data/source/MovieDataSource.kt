package com.uoooo.mvvm.example.data.source

import com.uoooo.mvvm.example.data.entity.Movie
import io.reactivex.Single

interface MovieDataSource {
    fun getPopularMovie(page: Int): Single<List<Movie>>
}
