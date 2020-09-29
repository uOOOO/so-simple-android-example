package com.uoooo.simple.example.data.util

import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

actual val IODispatcher: CoroutineContext = Dispatchers.Main
