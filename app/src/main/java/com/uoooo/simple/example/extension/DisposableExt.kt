package com.uoooo.simple.example.extension

import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

fun Disposable.disposeBy(disposer: CompositeDisposable) = this.apply { disposer.add(this) }
