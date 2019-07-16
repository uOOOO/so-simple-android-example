package com.uoooo.mvvm.example.data.source

import com.uoooo.mvvm.example.data.entity.Movie
import com.uoooo.mvvm.example.data.entity.Video
import io.reactivex.Single

interface MovieDataSource {
    fun getPopularMovie(page: Int): Single<List<Movie>>
    fun getVideos(id: Int): Single<List<Video>>
    fun getRecommendations(id: Int, page: Int): Single<List<Movie>>
}
