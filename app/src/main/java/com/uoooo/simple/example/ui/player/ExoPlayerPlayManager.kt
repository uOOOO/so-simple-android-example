package com.uoooo.simple.example.ui.player

import android.content.Context
import android.net.Uri
import android.util.Log
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.analytics.AnalyticsListener
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.drm.DefaultDrmSessionManager
import com.google.android.exoplayer2.drm.FrameworkMediaCrypto
import com.google.android.exoplayer2.drm.FrameworkMediaDrm
import com.google.android.exoplayer2.drm.UnsupportedDrmException
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ads.AdsLoader
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.PlayerView
import com.uoooo.simple.example.ui.player.rx.ExoPlayerEvent
import com.uoooo.simple.example.ui.player.rx.ExoPlayerVideo
import com.uoooo.simple.example.ui.player.rx.events
import com.uoooo.simple.example.ui.player.rx.videos
import io.reactivex.rxjava3.core.Observable
import java.net.CookieHandler
import java.net.CookieManager
import java.net.CookiePolicy
import kotlin.math.max

class ExoPlayerPlayManager {
    private var player: SimpleExoPlayer? = null
        private set
    private var mediaSource: MediaSource? = null
    private var mediaDrm: FrameworkMediaDrm? = null
    var trackSelector: DefaultTrackSelector? = null
        private set
    private var trackSelectorParameters: DefaultTrackSelector.Parameters? = null
    private var adsLoader: AdsLoader? = null
    private var playerView: PlayerView? = null

    private var startAutoPlay: Boolean = false
    private var startWindow: Int = 0
    private var startPosition: Long = 0

    private var adTagUri: Uri? = null

    init {
        if (CookieHandler.getDefault() !== DEFAULT_COOKIE_MANAGER) {
            CookieHandler.setDefault(DEFAULT_COOKIE_MANAGER)
        }
        clearStartPosition()
    }

    @Throws(UnsupportedDrmException::class)
    fun prepare(context: Context, playerView: PlayerView, licenseUrl: String?, uri: Uri, adTagUri: Uri?) {
        if (this.player == null) {
            val userAgent = context.applicationInfo.loadLabel(context.packageManager).toString()
            var drmSessionManager: DefaultDrmSessionManager<FrameworkMediaCrypto>? = null
            if (licenseUrl != null) {
                val drmCallback = ExoPlayerDrmHelper.buildMediaDrmCallback(
                    licenseUrl,
                    ExoPlayerBuildHelper.buildHttpDataSourceFactory(context, userAgent)
                )
                drmSessionManager = ExoPlayerBuildHelper.buildDrmSessionManagerV18(C.WIDEVINE_UUID, drmCallback)
            }
            val trackSelector = ExoPlayerBuildHelper.buildTrackSelector()
            val renderersFactory = ExoPlayerBuildHelper.buildRenderersFactory(context)
            val dataSourceFactory = ExoPlayerBuildHelper.buildDataSourceFactory(context, userAgent, false)
            var mediaSource = ExoPlayerBuildHelper.buildMediaSource(uri, dataSourceFactory)
            val player = ExoPlayerBuildHelper.buildPlayer(context, renderersFactory, trackSelector, drmSessionManager)

            if (adTagUri != null) {
                if (adTagUri != this.adTagUri) {
                    releaseAdsLoader()
                    this.adTagUri = adTagUri
                }
                adsLoader = ExoPlayerAdsHelper.createImaAdsLoader(context, player, adTagUri)
                if (adsLoader != null) {
                    mediaSource =
                        ExoPlayerAdsHelper.createAdsMediaSource(adsLoader!!, playerView, mediaSource, dataSourceFactory)
                } else {
                    Log.w(TAG, "Playing sample without ads, as the IMA extension was not loaded")
                }
            } else {
                releaseAdsLoader()
            }

            this.playerView = playerView
            this.playerView?.player = player
            this.player = player
            this.mediaSource = mediaSource
            this.trackSelector = trackSelector
            this.trackSelectorParameters = trackSelector.parameters
        }

        this.player?.run {
            setAudioFocus()
            val haveStartPosition = startWindow != C.INDEX_UNSET
            if (haveStartPosition) {
                seekTo(startWindow, startPosition)
            }
            prepare(mediaSource, !haveStartPosition, false)
        }
    }

    fun addAnalyticsListener(listener: AnalyticsListener) {
        player?.addAnalyticsListener(listener)
    }

    fun getEventListener(): Observable<ExoPlayerEvent>? {
        return player?.events()
    }

    fun getVideoListener(): Observable<ExoPlayerVideo>? {
        return player?.videos()
    }

    private fun updateTrackSelectorParameters() {
        trackSelector?.run {
            trackSelectorParameters = parameters
        }
    }

    private fun updateStartPosition() {
        player?.run {
            startAutoPlay = playWhenReady
            startWindow = currentWindowIndex
            startPosition = max(0, contentPosition)
        }
    }

    private fun clearStartPosition() {
        startAutoPlay = true
        startWindow = C.INDEX_UNSET
        startPosition = C.TIME_UNSET
    }

    private fun releaseMediaDrm() {
        mediaDrm?.run { release() }
    }

    private fun releaseAdsLoader() {
        adsLoader?.run {
            setPlayer(null)
            release()
            adsLoader = null
            adTagUri = null
            playerView?.overlayFrameLayout?.run { removeAllViews() }
        }
    }

    fun release() {
        player?.run {
            clearStartPosition()
            release()
            player = null
            trackSelector = null
        }
        playerView?.run {
            player = null
            playerView = null
        }
        releaseMediaDrm()
        releaseAdsLoader()
    }

    fun start() {
        if (hasPlayer()) {
            player?.playWhenReady = true
        }
    }

    fun pause() {
        if (hasPlayer()) {
            updateTrackSelectorParameters()
            updateStartPosition()
            player?.playWhenReady = false
        }
    }

    fun seekTo(seekPosition: Long) {
        if (hasPlayer()) {
            player?.seekTo(seekPosition)
        }
    }

    fun getCurrentPosition(): Long? {
        return if (hasPlayer()) player?.currentPosition else -1
    }

    fun getDuration(): Long {
        return if (hasPlayer()) player?.duration ?: -1 else -1
    }

    fun isPlaying(): Boolean {
        return hasPlayer() && player?.playWhenReady ?: false
    }

    fun hasPlayer(): Boolean {
        return player != null && playerView != null
    }

    private fun setAudioFocus() {
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(C.USAGE_MEDIA)
            .setContentType(C.CONTENT_TYPE_MOVIE)
            .build()
        player?.setAudioAttributes(audioAttributes, true)
    }

    companion object {
        private val TAG: String = ExoPlayerPlayManager::class.java.simpleName

        private val DEFAULT_COOKIE_MANAGER: CookieManager by lazy {
            CookieManager().apply {
                setCookiePolicy(CookiePolicy.ACCEPT_ORIGINAL_SERVER)
            }
        }
    }
}
