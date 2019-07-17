package com.uoooo.simple.example.data.source.remote.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.uoooo.simple.example.data.entity.Video

@JsonClass(generateAdapter = true)
data class VideosResponse(
    @Json(name = "id")
    val id: String?,

    @Json(name = "results")
    val results: List<Video> = listOf()
)
