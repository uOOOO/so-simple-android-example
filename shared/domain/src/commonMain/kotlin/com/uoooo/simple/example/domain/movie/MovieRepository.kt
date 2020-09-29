package com.uoooo.simple.example.domain.movie

interface MovieRepository {
    suspend fun getPopular(page: Int): List<Movie>
    suspend fun getVideos(id: Int): List<Video>
    suspend fun getRecommendations(id: Int, page: Int): List<Movie>
}
