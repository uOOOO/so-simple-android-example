package com.uoooo.mvvm.example.extension

import com.akaita.java.rxjava2debug.RxJava2Debug

fun Throwable.printEnhancedStackTrace() {
    RxJava2Debug.getEnhancedStackTrace(this).printStackTrace()
}
