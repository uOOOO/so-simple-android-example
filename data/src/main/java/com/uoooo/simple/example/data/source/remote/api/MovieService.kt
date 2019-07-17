package com.uoooo.simple.example.data.source.remote.api

import com.uoooo.simple.example.data.source.remote.response.MoviePopularResponse
import com.uoooo.simple.example.data.source.remote.response.MovieRecommendationsResponse
import com.uoooo.simple.example.data.source.remote.response.VideosResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieService {
    @GET("movie/popular")
    fun getPopularMovie(@Query("page") page: Int): Single<MoviePopularResponse>

    @GET("movie/{id}/videos")
    fun getVideos(@Path("id") id: Int): Single<VideosResponse>

    @GET("movie/{id}/recommendations")
    fun getRecommendations(@Path("id") id: Int, @Query("page") page: Int): Single<MovieRecommendationsResponse>
}
