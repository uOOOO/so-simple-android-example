package com.uoooo.simple.example.data.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.json.Json
import kotlin.coroutines.CoroutineContext

internal fun jsonParser() = Json {
    ignoreUnknownKeys = true
    isLenient = true
    useArrayPolymorphism = true
}

fun <T> T.toJson(serializer: SerializationStrategy<T>): String {
    return Json {
        encodeDefaults = false
        ignoreUnknownKeys = true
        isLenient = true
        useArrayPolymorphism = true
    }.run {
        return@run encodeToString(serializer, this@toJson)
    }
}

suspend inline fun <T, R> T.async(
    context: CoroutineContext = Dispatchers.Default,
    crossinline block: T.() -> R
): R {
    return withContext(context) { block() }
}

suspend fun <T> String.parseAsync(deserializer: KSerializer<T>): T {
    return async { jsonParser().decodeFromString(deserializer, this) }
}

fun <T> String.parse(deserializer: KSerializer<T>): T {
    return jsonParser().decodeFromString(deserializer, this)
}
