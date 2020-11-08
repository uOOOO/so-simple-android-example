package com.uoooo.simple.example.data.repository

import com.uoooo.simple.example.data.mapper.mapToModel
import com.uoooo.simple.example.data.source.remote.MovieDataSourceRemote
import com.uoooo.simple.example.domain.model.Movie
import com.uoooo.simple.example.domain.model.Video
import com.uoooo.simple.example.domain.repository.MovieRepository
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val dataSourceRemote: MovieDataSourceRemote
) : MovieRepository {
    override fun getPopular(page: Int): Single<List<Movie>> {
        return dataSourceRemote.getPopularMovie(page)
            .map { it ->
                it.asSequence()
                    .map { it.mapToModel() }
                    .filter { it != null }
                    .map { it!! }
                    .toList()
            }
    }

    override fun getVideos(id: Int): Single<List<Video>> {
        return dataSourceRemote.getVideos(id)
            .map { it ->
                it.asSequence()
                    .map { it.mapToModel() }
                    .filter { it != null }
                    .map { it!! }
                    .toList()
            }
    }

    override fun getRecommendations(id: Int, page: Int): Single<List<Movie>> {
        return dataSourceRemote.getRecommendations(id, page)
            .map { it ->
                it.asSequence()
                    .map { it.mapToModel() }
                    .filter { it != null }
                    .map { it!! }
                    .toList()
            }
    }
}
