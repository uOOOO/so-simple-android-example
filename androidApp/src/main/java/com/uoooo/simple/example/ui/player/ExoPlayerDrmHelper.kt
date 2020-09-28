package com.uoooo.simple.example.ui.player

import com.google.android.exoplayer2.drm.HttpMediaDrmCallback
import com.google.android.exoplayer2.drm.MediaDrmCallback
import com.google.android.exoplayer2.upstream.HttpDataSource

object ExoPlayerDrmHelper {
    fun buildMediaDrmCallback(licenseUrl: String, licenseDataSourceFactory: HttpDataSource.Factory): MediaDrmCallback {
        return HttpMediaDrmCallback(licenseUrl, licenseDataSourceFactory)
    }
}
