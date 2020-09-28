package com.uoooo.simple.example.ui.player

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.accessibility.CaptioningManager
import com.google.android.exoplayer2.text.CaptionStyleCompat
import com.google.android.exoplayer2.ui.SubtitleView
import com.google.android.exoplayer2.util.Util

object ExoPlayerCaptionHelper {
    @TargetApi(19)
    private fun getUserCaptionStyleV19(context: Context): CaptionStyleCompat {
        return (context.getSystemService(Context.CAPTIONING_SERVICE) as CaptioningManager?)?.run {
            return@run CaptionStyleCompat.createFromCaptionStyle(userStyle)
        } ?: CaptionStyleCompat.DEFAULT
    }

    @TargetApi(19)
    private fun getUserCaptionFontScaleV19(context: Context): Float {
        return (context.getSystemService(Context.CAPTIONING_SERVICE) as CaptioningManager?)?.run {
            return@run fontScale
        } ?: 1f
    }

    @SuppressLint("ObsoleteSdkInt")
    fun setCustomCaptionStyle(context: Context, subtitleView: SubtitleView?) {
        if (subtitleView == null) {
            return
        }

        val style =
            (if (Util.SDK_INT >= Build.VERSION_CODES.KITKAT)
                getUserCaptionStyleV19(context)
            else
                CaptionStyleCompat.DEFAULT)
                .run {
                    return@run CaptionStyleCompat(
                        foregroundColor,
                        Color.TRANSPARENT,
                        windowColor,
                        CaptionStyleCompat.EDGE_TYPE_OUTLINE,
                        Color.BLACK,
                        typeface
                    )
                }
        subtitleView.setStyle(style)

        val fontScale =
            if (Util.SDK_INT >= Build.VERSION_CODES.KITKAT)
                getUserCaptionFontScaleV19(context) * SubtitleView.DEFAULT_TEXT_SIZE_FRACTION
            else
                1f
        subtitleView.setFractionalTextSize(fontScale)
    }
}
