package com.uoooo.simple.example.data.source.remote

import com.uoooo.simple.example.data.entity.Movie
import com.uoooo.simple.example.data.entity.Video
import com.uoooo.simple.example.data.source.remote.api.MovieService
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class MovieDataSourceRemote @Inject constructor(private val movieService: MovieService) {
    fun getPopularMovie(page: Int): Single<List<Movie>> {
        return movieService.getPopularMovie(page)
            .map { it.results }
    }

    fun getVideos(id: Int): Single<List<Video>> {
        return movieService.getVideos(id)
            .map { it.results }
    }

    fun getRecommendations(id: Int, page: Int): Single<List<Movie>> {
        return movieService.getRecommendations(id, page)
            .map { it.results }
    }
}
