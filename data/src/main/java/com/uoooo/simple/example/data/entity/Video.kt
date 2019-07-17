package com.uoooo.simple.example.data.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

typealias VideoEntity = Video

@JsonClass(generateAdapter = true)
data class Video(
    @Json(name = "id")
    val id: String?,

    @Json(name = "key")
    val key: String?,

    @Json(name = "name")
    val name: String?,

    @Json(name = "site")
    val site: Site,

    @Json(name = "size")
    val size: Int = 0,

    @Json(name = "type")
    val type: Type
) {
    enum class Site {
        @Json(name = "Unknown")
        UNKNOWN,
        @Json(name = "YouTube")
        YOUTUBE
    }

    enum class Type {
        @Json(name = "Unknown")
        UNKNOWN,
        @Json(name = "Teaser")
        TEASER,
        @Json(name = "Trailer")
        TRAILER
    }
}
