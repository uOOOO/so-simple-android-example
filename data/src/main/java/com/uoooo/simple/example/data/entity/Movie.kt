package com.uoooo.simple.example.data.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

typealias MovieEntity = Movie

@JsonClass(generateAdapter = true)
data class Movie(
    @Json(name = "vote_count")
    val voteCount: Int = 0,

    @Json(name = "id")
    val id: Int = INVALID_ID,

    @Json(name = "video")
    val video: Boolean = false,

    @Json(name = "vote_average")
    val voteAverage: Float = 0f,

    @Json(name = "title")
    val title: String?,

    @Json(name = "popularity")
    val popularity: Float = 0f,

    @Json(name = "poster_path")
    val posterPath: String?,

    @Json(name = "original_language")
    val originalLanguage: String?,

    @Json(name = "original_title")
    val originalTitle: String?,

    @Json(name = "genre_ids")
    val genreIds: List<Int> = listOf(),

    @Json(name = "backdrop_path")
    val backdropPath: String?,

    @Json(name = "adult")
    val adult: Boolean = false,

    @Json(name = "overview")
    val overview: String?,

    @Json(name = "release_date")
    val releaseDate: String?
)

const val INVALID_ID = -1
