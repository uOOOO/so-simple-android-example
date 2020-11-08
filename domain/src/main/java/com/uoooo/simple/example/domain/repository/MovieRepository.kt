package com.uoooo.simple.example.domain.repository

import com.uoooo.simple.example.domain.model.Movie
import com.uoooo.simple.example.domain.model.Video
import io.reactivex.rxjava3.core.Single

interface MovieRepository {
    fun getPopular(page: Int): Single<List<Movie>>
    fun getVideos(id: Int): Single<List<Video>>
    fun getRecommendations(id: Int, page: Int): Single<List<Movie>>
}
