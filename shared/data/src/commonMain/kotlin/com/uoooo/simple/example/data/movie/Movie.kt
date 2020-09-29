package com.uoooo.simple.example.data.movie

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

typealias MovieEntity = Movie

@Serializable
data class Movie(
    @SerialName("vote_count")
    val voteCount: Int = 0,

    @SerialName("id")
    val id: Int = INVALID_ID,

    @SerialName("video")
    val video: Boolean = false,

    @SerialName("vote_average")
    val voteAverage: Float = 0f,

    @SerialName("title")
    val title: String?,

    @SerialName("popularity")
    val popularity: Float = 0f,

    @SerialName("poster_path")
    val posterPath: String?,

    @SerialName("original_language")
    val originalLanguage: String?,

    @SerialName("original_title")
    val originalTitle: String?,

    @SerialName("genre_ids")
    val genreIds: List<Int> = listOf(),

    @SerialName("backdrop_path")
    val backdropPath: String?,

    @SerialName("adult")
    val adult: Boolean = false,

    @SerialName("overview")
    val overview: String?,

    @SerialName("release_date")
    val releaseDate: String?
)

const val INVALID_ID = -1
