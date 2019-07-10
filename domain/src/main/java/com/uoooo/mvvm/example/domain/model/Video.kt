package com.uoooo.mvvm.example.domain.model

import java.io.Serializable

typealias VideoModel = Video

data class Video(
    val id: String,
    val key: String,
    val name: String,
    val site: Site,
    val size: Int,
    val type: Type
) : Serializable {
    enum class Site {
        UNKNOWN,
        YOUTUBE
    }

    enum class Type {
        UNKNOWN,
        TEASER,
        TRAILER
    }
}
