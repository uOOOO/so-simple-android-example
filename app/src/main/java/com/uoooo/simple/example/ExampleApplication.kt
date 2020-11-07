package com.uoooo.simple.example

import android.app.Application
import com.akaita.java.rxjava2debug.RxJava2Debug
import com.bumptech.glide.Glide
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ExampleApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initRxJava2Debug()
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
