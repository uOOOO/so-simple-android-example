package com.uoooo.simple.example.domain.interactor

import com.uoooo.simple.example.domain.model.Movie
import com.uoooo.simple.example.domain.model.Video
import io.reactivex.Single

interface GetMovieUseCase {
    fun getPopularMovie(page: Int): Single<List<Movie>>
    fun getVideos(id: Int): Single<List<Video>>
    fun getRecommendations(id: Int, page: Int): Single<List<Movie>>
}
