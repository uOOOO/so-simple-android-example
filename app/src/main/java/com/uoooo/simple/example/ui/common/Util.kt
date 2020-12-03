package com.uoooo.simple.example.ui.common

import com.uoooo.simple.example.data.ServerConfig
import com.uoooo.simple.example.domain.model.Movie

fun getPosterImageUrl(posterPath: String?, size: ServerConfig.ImageSize): String {
    return "${ServerConfig.IMAGE_BASE_URL}/$size/$posterPath"
}

fun averagePer100(movie: Movie): String =
    String.format("%d%%", (movie.voteAverage * 10).toInt())

fun averagePer10(movie: Movie) =
    String.format("%.1f", movie.voteAverage)