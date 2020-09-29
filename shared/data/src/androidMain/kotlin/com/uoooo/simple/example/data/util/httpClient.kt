package com.uoooo.simple.example.data.util

import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.example.android.BuildConfig
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.context.KoinContextHandler

actual val httpClient: HttpClient = HttpClient(OkHttp) {
//    install(HttpTimeout) {
//    }
    install(JsonFeature) {
        serializer = KotlinxSerializer(jsonParser())
    }
    engine {
        config {
            followRedirects(true)
            retryOnConnectionFailure(true)
        }
        if (BuildConfig.DEBUG) {
            addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            addInterceptor(ChuckerInterceptor(KoinContextHandler.get().get()))
        }
    }
}
