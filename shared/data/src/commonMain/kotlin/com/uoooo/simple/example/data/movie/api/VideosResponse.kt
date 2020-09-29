package com.uoooo.simple.example.data.movie.api

import com.uoooo.simple.example.data.movie.Video
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VideosResponse(
    @SerialName("id")
    val id: String?,

    @SerialName("results")
    val results: List<Video> = listOf()
)
