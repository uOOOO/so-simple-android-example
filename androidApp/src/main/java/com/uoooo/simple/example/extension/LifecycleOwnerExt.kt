package com.uoooo.simple.example.extension

import android.content.ComponentCallbacks
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import org.koin.android.ext.android.getKoin
import org.koin.core.definition.Definition
import org.koin.core.module.Module
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier
import kotlin.reflect.KClass

/**
 * The methods in this file were came from the following files.
 * org.koin.androidx.viewmodel.ViewModelResolution
 * org.koin.androidx.viewmodel.ext.android.LifecycleOwnerExt
 */

inline fun <reified T : LifecycleObserver> Module.lifecycleObserver(
    qualifier: Qualifier? = null,
    override: Boolean = false,
    noinline definition: Definition<T>
) {
    factory(qualifier, override, definition)
}

inline fun <reified T : LifecycleObserver> LifecycleOwner.lifecycleObserver(
    qualifier: Qualifier? = null,
    noinline parameters: ParametersDefinition? = null
): Lazy<T> = lazy {
    @Suppress("RemoveExplicitTypeArguments")
    getLifecycleObserver<T>(qualifier, parameters)
}

inline fun <reified T : LifecycleObserver> LifecycleOwner.getLifecycleObserver(
    qualifier: Qualifier? = null,
    noinline parameters: ParametersDefinition? = null
): T {
    return getLifecycleObserver(T::class, qualifier, parameters)
}

fun <T : LifecycleObserver> LifecycleOwner.getLifecycleObserver(
    clazz: KClass<T>,
    qualifier: Qualifier? = null,
    parameters: ParametersDefinition? = null
): T {
    return ((this as ComponentCallbacks).getKoin().get(clazz, qualifier, parameters) as T).apply {
        // https://github.com/googlecodelabs/android-lifecycles/issues/5
        lifecycle.addObserver(this)
    }
}
