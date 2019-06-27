package com.uoooo.mvvm.example.data.source

import com.uoooo.mvvm.example.data.entity.Movie
import com.uoooo.mvvm.example.data.source.remote.MovieDataSourceRemote
import io.reactivex.Single

class MovieDataSourceImpl constructor(private val remote: MovieDataSourceRemote) : MovieDataSource {
    override fun getPopularMovie(page: Int): Single<List<Movie>> {
        return remote.getPopularMovie(page)
    }
}
