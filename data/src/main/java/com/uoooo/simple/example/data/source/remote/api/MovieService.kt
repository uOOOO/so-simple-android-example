package com.uoooo.simple.example.data.source.remote.api

import com.uoooo.simple.example.data.source.remote.response.PopularMovieResponse
import com.uoooo.simple.example.data.source.remote.response.RecommendMovieResponse
import com.uoooo.simple.example.data.source.remote.response.VideosResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieService {
    @GET("movie/popular")
    fun getPopularMovie(@Query("page") page: Int): Single<PopularMovieResponse>

    @GET("movie/{id}/videos")
    fun getVideos(@Path("id") id: Int): Single<VideosResponse>

    @GET("movie/{id}/recommendations")
    fun getRecommendations(@Path("id") id: Int, @Query("page") page: Int): Single<RecommendMovieResponse>
}
