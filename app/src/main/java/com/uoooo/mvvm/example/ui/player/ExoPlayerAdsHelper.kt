package com.uoooo.mvvm.example.ui.player

import android.content.Context
import android.net.Uri
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ads.AdsLoader
import com.google.android.exoplayer2.source.ads.AdsMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DataSource

object ExoPlayerAdsHelper {
    fun createImaAdsLoader(context: Context, player: Player, adTagUri: Uri): AdsLoader? {
        // Load the extension source using reflection so the demo app doesn't have to depend on it.
        // The ads loader is reused for multiple playbacks, so that ad playback can resume.
        return try {
            val loaderClass = Class.forName("com.google.android.exoplayer2.ext.ima.ImaAdsLoader")
            // Full class names used so the LINT.IfChange rule triggers should any of the classes move.
            // LINT.IfChange
            val loaderConstructor = loaderClass
                .asSubclass(AdsLoader::class.java)
                .getConstructor(Context::class.java, Uri::class.java)
            // LINT.ThenChange(../../../../../../../../proguard-rules.txt)
            loaderConstructor.newInstance(context, adTagUri).apply {
                setPlayer(player)
            }
        } catch (e: ClassNotFoundException) {
            // IMA extension not loaded.
            null
        }
    }

    fun createAdsMediaSource(
        adsLoader: AdsLoader,
        playerView: PlayerView,
        mediaSource: MediaSource,
        dataSourceFactory: DataSource.Factory
    ): MediaSource {
        val adMediaSourceFactory = object : AdsMediaSource.MediaSourceFactory {
            override fun createMediaSource(uri: Uri): MediaSource {
                return ExoPlayerBuildHelper.buildMediaSource(uri, dataSourceFactory)
            }

            override fun getSupportedTypes(): IntArray {
                return intArrayOf(C.TYPE_DASH, C.TYPE_SS, C.TYPE_HLS, C.TYPE_OTHER)
            }
        }
        return AdsMediaSource(mediaSource, adMediaSourceFactory, adsLoader, playerView)
    }
}
