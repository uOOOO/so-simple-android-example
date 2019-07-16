package com.uoooo.mvvm.example.domain.interactor

import com.uoooo.mvvm.example.domain.model.Movie
import com.uoooo.mvvm.example.domain.model.Video
import com.uoooo.mvvm.example.domain.repository.MovieRepository
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
