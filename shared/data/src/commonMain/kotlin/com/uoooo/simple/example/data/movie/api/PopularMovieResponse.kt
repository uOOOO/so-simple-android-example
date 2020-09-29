package com.uoooo.simple.example.data.movie.api

import com.uoooo.simple.example.data.movie.Movie
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PopularMovieResponse(
    @SerialName("page")
    val page: Int,

    @SerialName("total_results")
    val totalResults: Int,

    @SerialName("total_pages")
    val totalPages: Int,

    @SerialName("results")
    val results: List<Movie> = listOf()
)
