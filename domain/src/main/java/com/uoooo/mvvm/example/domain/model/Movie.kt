package com.uoooo.mvvm.example.domain.model

import java.io.Serializable

typealias MovieModel = Movie

const val INVALID_ID = -1

data class Movie(
    val voteCount: Int = 0,
    val id: Int = INVALID_ID,
    val voteAverage: Float = 0f,
    val title: String,
    val popularity: Float = 0f,
    val posterPath: String?,
    val originalLanguage: String?,
    val originalTitle: String?,
    val genreIds: List<Int> = listOf(),
    val backdropPath: String?,
    val adult: Boolean = false,
    val overview: String?,
    val releaseDate: String?
) : Serializable
