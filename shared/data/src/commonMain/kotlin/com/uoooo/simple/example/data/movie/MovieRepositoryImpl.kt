package com.uoooo.simple.example.data.movie

import com.uoooo.simple.example.data.util.async
import com.uoooo.simple.example.domain.movie.Movie
import com.uoooo.simple.example.domain.movie.MovieRepository
import com.uoooo.simple.example.domain.movie.Video

class MovieRepositoryImpl(private val remoteSource: MovieDataSourceRemote) : MovieRepository {
    override suspend fun getPopular(page: Int): List<Movie> {
        return remoteSource.getPopular(page).results
            .async {
                asSequence()
                    .map { it.mapToModel() }
                    .filter { it != null }
                    .map { it!! }
                    .toList()
            }
    }

    override suspend fun getVideos(id: Int): List<Video> {
        return remoteSource.getVideos(id).results
            .async {
                asSequence()
                    .map { it.mapToModel() }
                    .filter { it != null }
                    .map { it!! }
                    .toList()
            }
    }

    override suspend fun getRecommendations(id: Int, page: Int): List<Movie> {
        return remoteSource.getRecommendations(id, page).results
            .async {
                asSequence()
                    .map { it.mapToModel() }
                    .filter { it != null }
                    .map { it!! }
                    .toList()
            }
    }
}
