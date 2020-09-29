package com.uoooo.simple.example.data.movie

import com.uoooo.simple.example.data.movie.api.MovieApi
import com.uoooo.simple.example.data.movie.api.PopularMovieResponse
import com.uoooo.simple.example.data.movie.api.RecommendMovieResponse
import com.uoooo.simple.example.data.movie.api.VideosResponse

class MovieDataSourceRemote(private val movieApi: MovieApi) {
    suspend fun getPopular(page: Int): PopularMovieResponse {
        return movieApi.getPopularMovie(page)
    }

    suspend fun getVideos(id: Int): VideosResponse {
        return movieApi.getVideos(id)
    }

    suspend fun getRecommendations(id: Int, page: Int): RecommendMovieResponse {
        return movieApi.getRecommendations(id, page)
    }
}
