package com.uoooo.simple.example.data.source.remote

import com.uoooo.simple.example.data.entity.Movie
import com.uoooo.simple.example.data.entity.Video
import com.uoooo.simple.example.data.source.remote.api.MovieService
import io.reactivex.Single

class MovieDataSourceRemoteImpl constructor(private val movieService: MovieService) : MovieDataSourceRemote {
    override fun getPopularMovie(page: Int): Single<List<Movie>> {
        return movieService.getPopularMovie(page)
            .map { it.results }
    }

    override fun getVideos(id: Int): Single<List<Video>> {
        return movieService.getVideos(id)
            .map { it.results }
    }

    override fun getRecommendations(id: Int, page: Int): Single<List<Movie>> {
        return movieService.getRecommendations(id, page)
            .map { it.results }
    }
}
