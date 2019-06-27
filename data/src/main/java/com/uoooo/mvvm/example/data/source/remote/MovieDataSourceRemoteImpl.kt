package com.uoooo.mvvm.example.data.source.remote

import com.uoooo.mvvm.example.data.entity.Movie
import com.uoooo.mvvm.example.data.source.remote.api.MovieService
import io.reactivex.Single

class MovieDataSourceRemoteImpl constructor(private val movieService: MovieService) : MovieDataSourceRemote {
    override fun getPopularMovie(page: Int): Single<List<Movie>> {
        return movieService.getPopularMovie(page)
            .map { it.results }
    }
}
