package com.uoooo.mvvm.example.data.source.remote

import com.uoooo.mvvm.example.data.entity.Movie
import io.reactivex.Single

interface MovieDataSourceRemote {
    fun getPopularMovie(page: Int): Single<List<Movie>>
}
