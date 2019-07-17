package com.uoooo.simple.example.ui.player.rx

import android.annotation.SuppressLint
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.video.VideoListener
import com.jakewharton.rxbinding3.internal.checkMainThread
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.MainThreadDisposable

fun SimpleExoPlayer.videos(): Observable<ExoPlayerVideo> {
    return ExoPlayerVideoObservable(this)
}

@SuppressLint("RestrictedApi")
private class ExoPlayerVideoObservable(
    private val player: SimpleExoPlayer
) : Observable<ExoPlayerVideo>() {
    override fun subscribeActual(observer: Observer<in ExoPlayerVideo>) {
        if (!checkMainThread(observer)) {
            return
        }
        val listener = Listener(player, observer)
        observer.onSubscribe(listener)
        player.addVideoListener(listener)
    }

    private class Listener(
        private val player: SimpleExoPlayer,
        private val observer: Observer<in ExoPlayerVideo>
    ) : MainThreadDisposable(), VideoListener {
        override fun onVideoSizeChanged(
            width: Int,
            height: Int,
            unappliedRotationDegrees: Int,
            pixelWidthHeightRatio: Float
        ) {
            if (!isDisposed) {
                observer.onNext(
                    ExoPlayerVideoVideoSizeChanged(
                        width,
                        height,
                        unappliedRotationDegrees,
                        pixelWidthHeightRatio
                    )
                )
            }
        }

        override fun onSurfaceSizeChanged(width: Int, height: Int) {
            if (!isDisposed) {
                observer.onNext(ExoPlayerVideoSurfaceSizeChanged(width, height))
            }
        }

        override fun onRenderedFirstFrame() {
            if (!isDisposed) {
                observer.onNext(ExoPlayerVideoRenderedFirstFrame)
            }
        }

        override fun onDispose() {
            player.removeVideoListener(this)
        }
    }
}

sealed class ExoPlayerVideo

data class ExoPlayerVideoVideoSizeChanged(
    val width: Int,
    val height: Int,
    val unappliedRotationDegrees: Int,
    val pixelWidthHeightRatio: Float
) : ExoPlayerVideo()

data class ExoPlayerVideoSurfaceSizeChanged(
    val width: Int,
    val height: Int
) : ExoPlayerVideo()

object ExoPlayerVideoRenderedFirstFrame : ExoPlayerVideo()