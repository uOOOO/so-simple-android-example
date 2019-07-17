package com.uoooo.simple.example.data.misc

import com.uoooo.simple.example.data.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class ApiKeyInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.request().run {
            newBuilder().url(url().run {
                newBuilder()
                    .addQueryParameter("api_key", BuildConfig.TMDB_API_KEY)
                    .build()
            }).build()
        }.run { chain.proceed(this) }
    }
}
