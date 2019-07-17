package com.uoooo.simple.example.domain.interactor

import com.uoooo.simple.example.domain.model.Movie
import com.uoooo.simple.example.domain.model.Video
import com.uoooo.simple.example.domain.repository.MovieRepository
import io.reactivex.Single

class GetMovieUseCaseImpl constructor(private val repository: MovieRepository) : GetMovieUseCase {
    override fun getPopularMovie(page: Int): Single<List<Movie>> {
        return repository.getPopular(page)
    }

    override fun getVideos(id: Int): Single<List<Video>> {
        return repository.getVideos(id)
    }

    override fun getRecommendations(id: Int, page: Int): Single<List<Movie>> {
        return repository.getRecommendations(id, page)
    }
}
