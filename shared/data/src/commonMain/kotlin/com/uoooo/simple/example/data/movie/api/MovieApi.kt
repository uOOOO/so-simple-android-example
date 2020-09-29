package com.uoooo.simple.example.data.movie.api

import com.uoooo.simple.example.data.C
import com.uoooo.simple.example.data.util.parseAsync
import io.ktor.client.*
import io.ktor.client.request.*

class MovieApi(private val httpClient: HttpClient, private val baseUrl: String = C.BASE_URL_API) {
    suspend fun getPopularMovie(page: Int): PopularMovieResponse {
        return httpClient
            .get<String>("${baseUrl.removeSuffix("/")}/movie/popular") {
                parameter("page", page)
            }
            .parseAsync(PopularMovieResponse.serializer())
    }

    suspend fun getVideos(id: Int): VideosResponse {
        return httpClient
            .get<String>("${baseUrl.removeSuffix("/")}/movie/$id/videos")
            .parseAsync(VideosResponse.serializer())
    }

    suspend fun getRecommendations(id: Int, page: Int): RecommendMovieResponse {
        return httpClient
            .get<String>("${baseUrl.removeSuffix("/")}/movie/$id/recommendations") {
                parameter("page", page)
            }
            .parseAsync(RecommendMovieResponse.serializer())
    }
}
