package com.uoooo.simple.example.extension

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

fun Disposable.disposeBy(disposer: CompositeDisposable) = this.apply { disposer.add(this) }
