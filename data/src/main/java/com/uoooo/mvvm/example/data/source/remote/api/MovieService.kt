package com.uoooo.mvvm.example.data.source.remote.api

import com.uoooo.mvvm.example.data.source.remote.response.MoviePopularResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieService {
    @GET("movie/popular")
    fun getPopularMovie(@Query("page") page: Int): Single<MoviePopularResponse>
}
