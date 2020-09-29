package com.uoooo.simple.example.domain

import co.touchlab.stately.freeze
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject
import kotlin.coroutines.CoroutineContext

open class UseCase : KoinComponent {
    protected val context: CoroutineContext by inject(Named.DISPATCH_IO)
    private val supervisorJob = SupervisorJob()

    val scope: CoroutineScope = object : CoroutineScope {
        override val coroutineContext: CoroutineContext
            get() = context + supervisorJob
    }
}

class SuspendSingleWrapper<T>(private val suspender: suspend () -> T) {
    fun subscribe(
        scope: CoroutineScope,
        onSuccess: (item: T) -> Unit,
        onThrow: (error: Throwable) -> Unit
    ): Job = scope.launch {
        try {
            onSuccess(suspender().freeze())
        } catch (error: Throwable) {
            onThrow(error.freeze())
        }
    }.freeze()
}

class SuspendCompletableWrapper(private val suspender: suspend () -> Unit) {
    fun subscribe(
        scope: CoroutineScope,
        onComplete: () -> Unit,
        onThrow: (error: Throwable) -> Unit
    ): Job = scope.launch {
        try {
            suspender()
            onComplete()
        } catch (error: Throwable) {
            onThrow(error.freeze())
        }
    }.freeze()
}