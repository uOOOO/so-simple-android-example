package com.uoooo.simple.example.data.util

import io.ktor.client.*
import io.ktor.client.engine.ios.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*

actual val httpClient: HttpClient = HttpClient(Ios) {
//    install(HttpTimeout) {
//    }
    install(JsonFeature) {
        serializer = KotlinxSerializer(jsonParser())
    }
    install(Logging) {
        level = LogLevel.BODY
    }
    engine {
        configureRequest {
            setAllowsCellularAccess(true)
        }
    }
}
