package com.uoooo.mvvm.example.data

object ServerConfig {
    const val API_BASE_URL = "https://api.themoviedb.org/3/"
    const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/"

    enum class ImageSize(val value: String) {
        NORMAL("w780"),
        BIG("w1280");

        override fun toString(): String {
            return value
        }
    }
}
