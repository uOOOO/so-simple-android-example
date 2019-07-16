package com.uoooo.mvvm.example.data.source.remote.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.uoooo.mvvm.example.data.entity.Movie

@JsonClass(generateAdapter = true)
data class MovieRecommendationsResponse(
    @Json(name = "page")
    val page: Int,

    @Json(name = "total_results")
    val totalResults: Int,

    @Json(name = "total_pages")
    val totalPages: Int,

    @Json(name = "results")
    val results: List<Movie> = listOf()
)
