package com.uoooo.simple.example.data.movie

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

typealias VideoEntity = Video

@Serializable
data class Video(
    @SerialName("id")
    val id: String?,

    @SerialName("key")
    val key: String?,

    @SerialName("name")
    val name: String?,

    @SerialName("site")
    val site: Site,

    @SerialName("size")
    val size: Int = 0,

    @SerialName("type")
    val type: Type
) {
    @Serializable
    enum class Site {
        @SerialName("Unknown")
        UNKNOWN,
        @SerialName("YouTube")
        YOUTUBE
    }

    @Serializable
    enum class Type {
        @SerialName("Unknown")
        UNKNOWN,
        @SerialName("Teaser")
        TEASER,
        @SerialName("Trailer")
        TRAILER
    }
}
