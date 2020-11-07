package com.uoooo.simple.example.domain.interactor

import com.uoooo.simple.example.domain.model.Movie
import com.uoooo.simple.example.domain.model.Video
import com.uoooo.simple.example.domain.repository.MovieRepository
import io.reactivex.Single
import javax.inject.Inject

class MovieUseCase @Inject constructor(private val repository: MovieRepository) {
    fun getPopularMovie(page: Int): Single<List<Movie>> {
        return repository.getPopular(page)
    }

    fun getVideos(id: Int): Single<List<Video>> {
        return repository.getVideos(id)
    }

    fun getRecommendations(id: Int, page: Int): Single<List<Movie>> {
        return repository.getRecommendations(id, page)
    }
}
