package com.uoooo.simple.example.ui.player

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Build
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.drm.*
import com.google.android.exoplayer2.offline.FilteringManifestParser
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.source.dash.manifest.DashManifestParser
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.source.hls.playlist.DefaultHlsPlaylistParserFactory
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource
import com.google.android.exoplayer2.source.smoothstreaming.manifest.SsManifestParser
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.*
import com.google.android.exoplayer2.util.Util
import java.util.*

object ExoPlayerBuildHelper {
    private fun buildUdpDataSourceFactory(): DataSource.Factory {
        return DataSource.Factory { UdpDataSource() }
    }

    fun buildHttpDataSourceFactory(context: Context, userAgent: String?): HttpDataSource.Factory {
        return DefaultHttpDataSourceFactory(Util.getUserAgent(context, userAgent))
    }

    fun buildDataSourceFactory(context: Context, userAgent: String, isUdp: Boolean): DataSource.Factory {
        return DefaultDataSourceFactory(
            context,
            if (isUdp) buildUdpDataSourceFactory() else buildHttpDataSourceFactory(context, userAgent)
        )
    }

    @SuppressLint("SwitchIntDef")
    fun buildMediaSource(uri: Uri, dataSourceFactory: DataSource.Factory): MediaSource {
        when (@C.ContentType val type = Util.inferContentType(uri, null)) {
            C.TYPE_DASH -> return DashMediaSource.Factory(dataSourceFactory)
                .setManifestParser(
                    FilteringManifestParser(DashManifestParser(), null)
                )
                .createMediaSource(uri)
            C.TYPE_SS -> return SsMediaSource.Factory(dataSourceFactory)
                .setManifestParser(
                    FilteringManifestParser(SsManifestParser(), null)
                )
                .createMediaSource(uri)
            C.TYPE_HLS -> return HlsMediaSource.Factory(dataSourceFactory)
                .setPlaylistParserFactory(
                    DefaultHlsPlaylistParserFactory()
                )
                .createMediaSource(uri)
            C.TYPE_OTHER -> return ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(uri)
            else -> {
                throw IllegalStateException("Unsupported type: $type")
            }
        }
    }

    @Throws(UnsupportedDrmException::class)
    private fun buildFrameworkMediaDrm(uuid: UUID): FrameworkMediaDrm {
        return FrameworkMediaDrm.newInstance(uuid)
    }

    @SuppressLint("ObsoleteSdkInt")
    @Throws(UnsupportedDrmException::class)
    fun buildDrmSessionManagerV18(
        uuid: UUID,
        mediaDrmCallback: MediaDrmCallback
    ): DefaultDrmSessionManager<FrameworkMediaCrypto> {
        if (Util.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
            throw RuntimeException("Protected content not supported on API levels below 18")
        }
        return DefaultDrmSessionManager(
            uuid, buildFrameworkMediaDrm(uuid), mediaDrmCallback, null, false
        )
    }

    private fun buildTrackSelectorParameter(): DefaultTrackSelector.Parameters {
        return DefaultTrackSelector.ParametersBuilder()
            .setPreferredTextLanguage(Locale.getDefault().language)
            .build()
    }

    fun buildTrackSelector(): DefaultTrackSelector {
        val trackSelectionFactory = AdaptiveTrackSelection.Factory()
        return DefaultTrackSelector(trackSelectionFactory).apply {
            parameters = buildTrackSelectorParameter()
        }
    }

    fun buildRenderersFactory(context: Context): DefaultRenderersFactory {
        return DefaultRenderersFactory(context).apply {
            @DefaultRenderersFactory.ExtensionRendererMode val extensionRendererMode =
                DefaultRenderersFactory.EXTENSION_RENDERER_MODE_OFF
            setExtensionRendererMode(extensionRendererMode)
        }
    }

    fun buildPlayer(
        context: Context,
        defaultRenderersFactory: DefaultRenderersFactory,
        trackSelector: DefaultTrackSelector,
        drmSessionManager: DefaultDrmSessionManager<FrameworkMediaCrypto>?
    ): SimpleExoPlayer {
        return ExoPlayerFactory.newSimpleInstance(
            context, defaultRenderersFactory,
            trackSelector, drmSessionManager
        )
    }

//    @Throws(UnsupportedDrmException::class)
//    fun newPlayerInstance(
//        context: Context,
//        uri: Uri,
//        licenseUrl: String?,
//        isUdp: Boolean
//    ): Triple<SimpleExoPlayer, MediaSource, DefaultTrackSelector> {
//        val userAgent = context.applicationInfo.loadLabel(context.packageManager).toString()
//        var drmSessionManager: DefaultDrmSessionManager<FrameworkMediaCrypto>? = null
//        if (licenseUrl != null) {
//            val drmCallback = ExoPlayerDrmHelper.buildMediaDrmCallback(
//                licenseUrl,
//                buildHttpDataSourceFactory(context, userAgent)
//            )
//            drmSessionManager = buildDrmSessionManagerV18(C.WIDEVINE_UUID, drmCallback)
//        }
//        val trackSelector = buildTrackSelector()
//        val renderersFactory = buildRenderersFactory(context)
//        val dataSourceFactory = buildDataSourceFactory(context, userAgent, isUdp)
//        val mediaSource = buildMediaSource(uri, dataSourceFactory)
//        val player = buildPlayer(context, renderersFactory, trackSelector, drmSessionManager)
//        return Triple(player, mediaSource, trackSelector)
//    }
}