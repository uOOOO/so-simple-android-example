package com.uoooo.simple.example.ui.viewmodel

import android.app.Application
import android.content.Context
import android.net.Uri
import android.util.SparseArray
import androidx.hilt.lifecycle.ViewModelInject
import at.huber.youtubeExtractor.VideoMeta
import at.huber.youtubeExtractor.YouTubeExtractor
import at.huber.youtubeExtractor.YtFile
import com.uoooo.simple.example.domain.model.Video
import com.uoooo.simple.example.domain.repository.MovieRepository
import com.uoooo.simple.example.ui.common.BaseViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

// TODO : need view state - loading, error...
class VideoViewModel @ViewModelInject constructor(
    @ApplicationContext context: Context,
    private val repository: MovieRepository
) : BaseViewModel(context as Application) {
    fun getYouTubeVideo(id: Int): Single<Uri> {
        return repository.getVideos(id)
            .flatMapObservable { Observable.fromIterable(it) }
            .filter { it.site == Video.Site.YOUTUBE }
            .toList()
            .flatMap { getYoutubeLink(it[0].key) }
            .subscribeOn(Schedulers.io())
    }

    private fun getYoutubeLink(key: String): Single<Uri> {
        return Single.create {
            CustomYouTubeExtractor(getApplication(), object : CustomYouTubeExtractor.Listener {
                override fun onExtractionComplete(
                    ytFiles: SparseArray<YtFile>?,
                    videoMeta: VideoMeta?
                ) {
                    if (it.isDisposed) {
                        return
                    }
                    try {
                        val downloadUrl = ytFiles?.get(22)?.url
                        if (!it.isDisposed) it.onSuccess(Uri.parse(downloadUrl))
                    } catch (e: Exception) {
                        if (!it.isDisposed) it.onError(RuntimeException("Didn't extract Youtube link."))
                    }
                }
            }).extract("http://youtube.com/watch?v=$key", false, false)
        }
    }

    private class CustomYouTubeExtractor(context: Context, private val listener: Listener) :
        YouTubeExtractor(context) {
        interface Listener {
            fun onExtractionComplete(ytFiles: SparseArray<YtFile>?, videoMeta: VideoMeta?)
        }

        override fun onExtractionComplete(ytFiles: SparseArray<YtFile>?, videoMeta: VideoMeta?) {
            listener.onExtractionComplete(ytFiles, videoMeta)
        }
    }
}
