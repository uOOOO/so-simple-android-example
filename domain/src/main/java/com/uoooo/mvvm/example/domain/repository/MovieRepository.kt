package com.uoooo.mvvm.example.domain.repository

import com.uoooo.mvvm.example.domain.model.Movie
import com.uoooo.mvvm.example.domain.model.Video
import io.reactivex.Single

interface MovieRepository {
    fun getPopular(page: Int): Single<List<Movie>>
    fun getVideos(id: Int): Single<List<Video>>
    fun getRecommendations(id: Int, page: Int): Single<List<Movie>>
}
