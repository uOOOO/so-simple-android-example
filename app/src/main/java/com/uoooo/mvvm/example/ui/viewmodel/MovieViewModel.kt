package com.uoooo.mvvm.example.ui.viewmodel

import android.app.Application
import android.content.Context
import android.net.Uri
import android.util.SparseArray
import androidx.paging.PagedList
import androidx.paging.RxPagedListBuilder
import at.huber.youtubeExtractor.VideoMeta
import at.huber.youtubeExtractor.YouTubeExtractor
import at.huber.youtubeExtractor.YtFile
import com.uoooo.mvvm.example.domain.model.Movie
import com.uoooo.mvvm.example.domain.model.Video
import com.uoooo.mvvm.example.domain.repository.MovieRepository
import com.uoooo.mvvm.example.ui.common.BaseViewModel
import com.uoooo.mvvm.example.ui.movie.source.PopularMovieDataSourceFactory
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

// TODO : need view state - loading, error...
class MovieViewModel(application: Application, private val repository: MovieRepository) : BaseViewModel(application) {
    // TODO : need DI?
    private val popularMovieDataSourceFactory by lazy {
        PopularMovieDataSourceFactory(repository)
    }

    fun getPopularMovieList(): Flowable<PagedList<Movie>> {
        val config = PagedList.Config.Builder()
            .setPageSize(10)
            .setEnablePlaceholders(false)
            .build()
        return RxPagedListBuilder(popularMovieDataSourceFactory, config)
            .buildFlowable(BackpressureStrategy.LATEST)
    }

    fun getVideos(id: Int): Single<List<Video>> {
        return repository.getVideos(id)
            .subscribeOn(Schedulers.io())
    }

    fun getYoutubeLink(context: Context, key: String): Single<Uri> {
        return Single.create {
            CustomYouTubeExtractor(context, object : CustomYouTubeExtractor.Listener {
                override fun onExtractionComplete(ytFiles: SparseArray<YtFile>?, videoMeta: VideoMeta?) {
                    if (it.isDisposed) {
                        return
                    }
                    try {
                        val downloadUrl = ytFiles?.get(137)?.url ?: ytFiles?.get(136)?. url
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

    override fun onCleared() {
        super.onCleared()
        popularMovieDataSourceFactory.onCleared()
    }
}
