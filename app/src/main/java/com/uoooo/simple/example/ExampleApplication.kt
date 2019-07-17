package com.uoooo.simple.example

import android.app.Application
import com.akaita.java.rxjava2debug.RxJava2Debug
import com.bumptech.glide.Glide
import com.uoooo.simple.example.data.di.dataSourceModule
import com.uoooo.simple.example.data.di.repositoryModule
import com.uoooo.simple.example.data.di.webServiceModule
import com.uoooo.simple.example.di.viewModelModule
import com.uoooo.simple.example.domain.di.interactorModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.logger.AndroidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class ExampleApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin()
        initRxJava2Debug()
    }

    private fun initKoin() {
        startKoin {
            androidContext(this@ExampleApplication)
            logger(if (BuildConfig.DEBUG) AndroidLogger(Level.DEBUG) else AndroidLogger())
            modules(listOf(webServiceModule, dataSourceModule, repositoryModule, interactorModule, viewModelModule))
        }
    }

    private fun initRxJava2Debug() {
        RxJava2Debug.enableRxJava2AssemblyTracking()
    }

    override fun onLowMemory() {
        Glide.get(this).onLowMemory()
        super.onLowMemory()
    }

    override fun onTrimMemory(level: Int) {
        Glide.get(this).onTrimMemory(level)
        super.onTrimMemory(level)
    }
}
