package com.uoooo.simple.example.data.source

import com.uoooo.simple.example.data.entity.Movie
import com.uoooo.simple.example.data.entity.Video
import com.uoooo.simple.example.data.source.remote.MovieDataSourceRemote
import io.reactivex.Single

class MovieDataSourceImpl constructor(private val remote: MovieDataSourceRemote) : MovieDataSource {
    override fun getPopularMovie(page: Int): Single<List<Movie>> {
        return remote.getPopularMovie(page)
    }

    override fun getVideos(id: Int): Single<List<Video>> {
        return remote.getVideos(id)
    }

    override fun getRecommendations(id: Int, page: Int): Single<List<Movie>> {
        return remote.getRecommendations(id, page)
    }
}
