package com.uoooo.mvvm.example.ui.player.rx

import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.jakewharton.rxbinding3.internal.checkMainThread
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.MainThreadDisposable

fun SimpleExoPlayer.events(): Observable<ExoPlayerEvent> {
    return ExoPlayerEventObservable(this)
}

private class ExoPlayerEventObservable(
    private val player: SimpleExoPlayer
) : Observable<ExoPlayerEvent>() {
    override fun subscribeActual(observer: Observer<in ExoPlayerEvent>) {
        if (!checkMainThread(observer)) {
            return
        }
        val listener = Listener(player, observer)
        observer.onSubscribe(listener)
        player.addListener(listener)
    }

    private class Listener(
        private val player: SimpleExoPlayer,
        private val observer: Observer<in ExoPlayerEvent>
    ) : MainThreadDisposable(), Player.EventListener {
        override fun onTimelineChanged(timeline: Timeline?, manifest: Any?, reason: Int) {
            if (!isDisposed) {
                observer.onNext(
                    ExoPlayerEventTimelineChanged(
                        timeline,
                        manifest,
                        reason
                    )
                )
            }
        }

        override fun onTracksChanged(trackGroups: TrackGroupArray?, trackSelections: TrackSelectionArray?) {
            if (!isDisposed) {
                observer.onNext(
                    ExoPlayerEventTracksChanged(
                        trackGroups,
                        trackSelections
                    )
                )
            }
        }

        override fun onLoadingChanged(isLoading: Boolean) {
            if (!isDisposed) {
                observer.onNext(ExoPlayerEventLoadingChanged(isLoading))
            }
        }

        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            if (!isDisposed) {
                observer.onNext(
                    ExoPlayerEventPlayerStateChanged(
                        playWhenReady,
                        playbackState
                    )
                )
            }
        }

        override fun onRepeatModeChanged(repeatMode: Int) {
            if (!isDisposed) {
                observer.onNext(ExoPlayerEventRepeatModeChanged(repeatMode))
            }
        }

        override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {
            if (!isDisposed) {
                observer.onNext(
                    ExoPlayerEventShuffleModeEnabledChanged(
                        shuffleModeEnabled
                    )
                )
            }
        }

        override fun onPlayerError(error: ExoPlaybackException?) {
            if (!isDisposed) {
                observer.onNext(ExoPlayerEventPlayerError(error))
            }
        }

        override fun onPositionDiscontinuity(reason: Int) {
            if (!isDisposed) {
                observer.onNext(ExoPlayerEventPositionDiscontinuity(reason))
            }
        }

        override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters?) {
            if (!isDisposed) {
                observer.onNext(
                    ExoPlayerEventPlaybackParametersChanged(
                        playbackParameters
                    )
                )
            }
        }

        override fun onSeekProcessed() {
            if (!isDisposed) {
                observer.onNext(ExoPlayerSeekProcessed)
            }
        }

        override fun onDispose() {
            player.removeListener(this)
        }
    }
}

sealed class ExoPlayerEvent

data class ExoPlayerEventTimelineChanged(
    val timeline: Timeline?,
    val manifest: Any?,
    @Player.TimelineChangeReason val reason: Int
) : ExoPlayerEvent()

data class ExoPlayerEventTracksChanged(
    val trackGroups: TrackGroupArray?,
    val trackSelections: TrackSelectionArray?
) : ExoPlayerEvent()

data class ExoPlayerEventLoadingChanged(
    val isLoading: Boolean
) : ExoPlayerEvent()

data class ExoPlayerEventPlayerStateChanged(
    val playWhenReady: Boolean,
    val playbackState: Int
) : ExoPlayerEvent()

data class ExoPlayerEventRepeatModeChanged(
    @Player.RepeatMode val repeatMode: Int
) : ExoPlayerEvent()

data class ExoPlayerEventShuffleModeEnabledChanged(
    val shuffleModeEnabledChanged: Boolean
) : ExoPlayerEvent()

data class ExoPlayerEventPlayerError(
    val error: ExoPlaybackException?
) : ExoPlayerEvent()

data class ExoPlayerEventPositionDiscontinuity(
    @Player.DiscontinuityReason val reason: Int
) : ExoPlayerEvent()

data class ExoPlayerEventPlaybackParametersChanged(
    val playbackParameters: PlaybackParameters?
) : ExoPlayerEvent()

object ExoPlayerSeekProcessed : ExoPlayerEvent()