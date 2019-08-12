package com.uoooo.simple.example.data.repository

import com.uoooo.simple.example.data.mapper.mapToModel
import com.uoooo.simple.example.data.source.MovieDataSource
import com.uoooo.simple.example.domain.model.Movie
import com.uoooo.simple.example.domain.model.Video
import com.uoooo.simple.example.domain.repository.MovieRepository
import io.reactivex.Single

class MovieRepositoryImpl constructor(
    private val dataSource: MovieDataSource
) : MovieRepository {
    override fun getPopular(page: Int): Single<List<Movie>> {
        return dataSource.getPopularMovie(page)
            .map { it ->
                it.asSequence()
                    .map { it.mapToModel() }
                    .filter { it != null }
                    .map { it!! }
                    .toList()
            }
    }

    override fun getVideos(id: Int): Single<List<Video>> {
        return dataSource.getVideos(id)
            .map { it ->
                it.asSequence()
                    .map { it.mapToModel() }
                    .filter { it != null }
                    .map { it!! }
                    .toList()
            }
    }

    override fun getRecommendations(id: Int, page: Int): Single<List<Movie>> {
        return dataSource.getRecommendations(id, page)
            .map { it ->
                it.asSequence()
                    .map { it.mapToModel() }
                    .filter { it != null }
                    .map { it!! }
                    .toList()
            }
    }
}