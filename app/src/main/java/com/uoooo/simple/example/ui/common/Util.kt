package com.uoooo.simple.example.ui.common

import com.uoooo.simple.example.data.ServerConfig

fun getPosterImageUrl(posterPath: String?, size: ServerConfig.ImageSize): String {
    return "${ServerConfig.IMAGE_BASE_URL}/$size/$posterPath"
}
