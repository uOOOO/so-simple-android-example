package com.uoooo.mvvm.example.ui.common

import com.uoooo.mvvm.example.data.ServerConfig

fun getPosterImageUrl(posterPath: String?, size: ServerConfig.ImageSize): String {
    return "${ServerConfig.IMAGE_BASE_URL}/$size/$posterPath"
}
