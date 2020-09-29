package com.uoooo.simple.example.domain.movie

import com.uoooo.simple.example.domain.SuspendSingleWrapper
import com.uoooo.simple.example.domain.UseCase
import org.koin.core.inject

class GetMovieUseCase : UseCase() {
    private val repository: MovieRepository by inject()

    fun getPopularMovie(page: Int): SuspendSingleWrapper<List<Movie>> {
        return SuspendSingleWrapper {
            repository.getPopular(page)
        }
    }

    fun getVideos(id: Int): SuspendSingleWrapper<List<Video>> {
        return SuspendSingleWrapper {
            repository.getVideos(id)
        }
    }

    fun getRecommendations(id: Int, page: Int): SuspendSingleWrapper<List<Movie>> {
        return SuspendSingleWrapper {
            repository.getRecommendations(id, page)
        }
    }
}
