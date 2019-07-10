package com.uoooo.mvvm.example.ui.detail

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.util.EventLogger
import com.jakewharton.rxbinding3.view.clicks
import com.uoooo.mvvm.example.GlideApp
import com.uoooo.mvvm.example.R
import com.uoooo.mvvm.example.data.ServerConfig
import com.uoooo.mvvm.example.domain.model.Movie
import com.uoooo.mvvm.example.domain.model.Video
import com.uoooo.mvvm.example.extension.printEnhancedStackTrace
import com.uoooo.mvvm.example.ui.common.getPosterImageUrl
import com.uoooo.mvvm.example.ui.player.ExoPlayerPlayManager
import com.uoooo.mvvm.example.ui.player.rx.*
import com.uoooo.mvvm.example.ui.viewmodel.MovieViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.sellmair.disposer.Disposer
import io.sellmair.disposer.disposeBy
import io.sellmair.disposer.onDestroy
import kotlinx.android.synthetic.main.exo_simple_player_view.view.*
import kotlinx.android.synthetic.main.fragment_detail.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class DetailFragment : Fragment() {
    private val movieViewModel: MovieViewModel by viewModel()
    private val playManager: ExoPlayerPlayManager by lazy {
        ExoPlayerPlayManager()
    }
    private val listenerDisposer = Disposer.create(onDestroy)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val movie: Movie = (arguments?.getSerializable(BUNDLE_OBJECT) ?: return) as Movie

        GlideApp.with(playerView.exo_backdrop)
            .load(getPosterImageUrl(movie.backdropPath, ServerConfig.ImageSize.NORMAL))
            .transition(DrawableTransitionOptions.withCrossFade())
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(playerView.exo_backdrop)

        playerView.useController = false
        playerView.controllerShowTimeoutMs = 1000

        loadVideoData(movie.id)
    }

    private fun loadVideoData(id: Int) {
        context?.let { context ->
            movieViewModel.getVideos(id)
                .flatMap { it ->
                    Observable.fromIterable(it)
                        .filter { it.site == Video.Site.YOUTUBE }
                        .toList()
                }
                .flatMap { movieViewModel.getYoutubeLink(context, it[0].key) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({uri ->
                    bindPlayButton(uri)
                    showInteractionView()
                }, {
                    it.printEnhancedStackTrace()
                    // TODO : show error message
                })
                .disposeBy(onDestroy)
        }
    }

    private fun playerStart(uri: Uri) {
        context?.let { context ->
            hideInteractionView()
            playerView.useController = true
            playManager.prepare(context, playerView, null, uri, null)
            playManager.start()
            bindPlayerListeners()
        }
    }

    private fun playerPause() {
        playManager.pause()
    }

    private fun playerRelease() {
        playManager.release()
    }

    private fun bindPlayButton(uri: Uri) {
        playButton.clicks()
            .subscribe { playerStart(uri) }
            .disposeBy(onDestroy)
    }

    private fun bindPlayerListeners() {
        playManager.addAnalyticsListener(EventLogger(playManager.trackSelector))
        playManager.player?.apply {
            listenerDisposer += events().subscribe { onExoPlayerEventListener(it) }
            listenerDisposer += videos().subscribe { onExoPlayerVideoListener(it) }
        }
    }

    private fun showInteractionView() {
        playButton.resumeAnimation()
        interactionGroup.visibility = View.VISIBLE
    }

    private fun hideInteractionView() {
        interactionGroup.visibility = View.GONE
        playButton.pauseAnimation()
    }

    private fun showBackdropImage() {
        playerView.exo_backdrop.visibility = View.VISIBLE
    }

    private fun hideBackdropImage() {
        playerView.exo_backdrop.visibility = View.INVISIBLE
    }

    override fun onStop() {
        playerPause()
        showBackdropImage()
        playerView.showController()
        playerView.controllerHideOnTouch = false
        super.onStop()
    }

    override fun onDestroy() {
        playerRelease()
        super.onDestroy()
    }

    private fun onExoPlayerEventListener(event: ExoPlayerEvent) {
        when (event) {
            is ExoPlayerEventPlayerStateChanged -> onPlayerStateChanged(event)
        }
    }

    private fun onPlayerStateChanged(event: ExoPlayerEventPlayerStateChanged) {
        Log.d(TAG, "onPlayerStateChanged() event = $event")
        when (event.playbackState) {
            Player.STATE_READY -> {
                hideBackdropImage()
                hideInteractionView()
                playerView.controllerHideOnTouch = true
            }
            Player.STATE_ENDED -> {
                listenerDisposer.dispose()
                playerRelease()
                showBackdropImage()
            }
        }
    }

    private fun onExoPlayerVideoListener(event: ExoPlayerVideo) {
        when (event) {
            is ExoPlayerVideoRenderedFirstFrame -> onRenderedFirstFrame()
        }
    }

    private fun onRenderedFirstFrame() {
        hideInteractionView()
    }

    companion object {
        private val TAG: String = DetailFragment::class.java.simpleName

        private const val BUNDLE_OBJECT = "movie"

        fun newInstance(movie: Movie): DetailFragment {
            return DetailFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(BUNDLE_OBJECT, movie)
                }
            }
        }
    }
}
