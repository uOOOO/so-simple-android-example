package com.uoooo.mvvm.example.ui.viewmodel

import android.app.Application
import android.content.Context
import android.net.Uri
import android.util.SparseArray
import at.huber.youtubeExtractor.VideoMeta
import at.huber.youtubeExtractor.YouTubeExtractor
import at.huber.youtubeExtractor.YtFile
import com.uoooo.mvvm.example.domain.model.Video
import com.uoooo.mvvm.example.domain.repository.MovieRepository
import com.uoooo.mvvm.example.ui.common.BaseViewModel
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

// TODO : need view state - loading, error...
class MovieVideoViewModel(application: Application, private val repository: MovieRepository) :
    BaseViewModel(application) {

    fun getYouTubeVideo(context: Context, id: Int): Single<Uri> {
        return repository.getVideos(id)
            .flatMapObservable { Observable.fromIterable(it) }
            .filter { it.site == Video.Site.YOUTUBE }
            .toList()
            .flatMap { getYoutubeLink(context, it[0].key) }
            .subscribeOn(Schedulers.io())
    }

    private fun getYoutubeLink(context: Context, key: String): Single<Uri> {
        return Single.create {
            CustomYouTubeExtractor(context, object : CustomYouTubeExtractor.Listener {
                override fun onExtractionComplete(ytFiles: SparseArray<YtFile>?, videoMeta: VideoMeta?) {
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

    private class CustomYouTubeExtractor(context: Context, private val listener: Listener) : YouTubeExtractor(context) {
        interface Listener {
            fun onExtractionComplete(ytFiles: SparseArray<YtFile>?, videoMeta: VideoMeta?)
        }

        override fun onExtractionComplete(ytFiles: SparseArray<YtFile>?, videoMeta: VideoMeta?) {
            listener.onExtractionComplete(ytFiles, videoMeta)
        }
    }
}