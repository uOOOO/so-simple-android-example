package com.uoooo.mvvm.example.data.repository

import com.uoooo.mvvm.example.data.mapper.mapToModel
import com.uoooo.mvvm.example.data.source.MovieDataSource
import com.uoooo.mvvm.example.domain.model.Movie
import com.uoooo.mvvm.example.domain.model.Video
import com.uoooo.mvvm.example.domain.repository.MovieRepository
import io.reactivex.Single

class MovieRepositoryImpl constructor(
    private val dataSource: MovieDataSource
) : MovieRepository {
    override fun getPopularMovie(page: Int): Single<List<Movie>> {
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
}
