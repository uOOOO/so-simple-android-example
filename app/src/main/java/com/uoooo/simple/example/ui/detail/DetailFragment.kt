package com.uoooo.simple.example.ui.detail

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.util.Pair
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.util.EventLogger
import com.uoooo.simple.example.GlideApp
import com.uoooo.simple.example.R
import com.uoooo.simple.example.data.ServerConfig
import com.uoooo.simple.example.domain.model.Movie
import com.uoooo.simple.example.extension.printEnhancedStackTrace
import com.uoooo.simple.example.ui.common.getPosterImageUrl
import com.uoooo.simple.example.ui.movie.MovieAdapter
import com.uoooo.simple.example.ui.player.ExoPlayerPlayManager
import com.uoooo.simple.example.ui.player.rx.*
import com.uoooo.simple.example.ui.viewmodel.MovieVideoViewModel
import com.uoooo.simple.example.ui.viewmodel.RecommendationListViewModel
import com.uoooo.simple.example.ui.viewmodel.state.PagingState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject
import io.sellmair.disposer.Disposer
import io.sellmair.disposer.disposeBy
import io.sellmair.disposer.onDestroy
import kotlinx.android.synthetic.main.exo_simple_player_view.view.*
import kotlinx.android.synthetic.main.fragment_detail.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailFragment : Fragment() {
    private val recommendationListViewModel: RecommendationListViewModel by viewModel()
    private val movieVideoViewModel: MovieVideoViewModel by viewModel()
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

        initVideo(movie.backdropPath)
        initRecommendationList(movie.id)

        loadVideoData(movie.id)
    }

    private fun initVideo(backdropPath: String?) {
        GlideApp.with(playerView.exo_backdrop)
            .load(getPosterImageUrl(backdropPath, ServerConfig.ImageSize.NORMAL))
            .transition(DrawableTransitionOptions.withCrossFade())
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(playerView.exo_backdrop)

        playerView.useController = false
        playerView.controllerShowTimeoutMs = 1500
        // TODO : error message
        playerView.setErrorMessageProvider { Pair.create(-1, "Error") }
    }

    private fun initRecommendationList(id: Int) {
        val itemClickObserver = PublishSubject.create<Movie>().apply {
            subscribe {
                onDestroy.dispose()
                playerRelease()

                arguments = Bundle().apply {
                    putSerializable(BUNDLE_OBJECT, it)
                }
                fragmentManager?.run {
                    this.beginTransaction().detach(this@DetailFragment).attach(this@DetailFragment).commit()
                }
            }.disposeBy(onDestroy)
        }

        val adapter = MovieAdapter(itemClickObserver)

        recommendationList.apply {
            setHasFixedSize(true)
            this.adapter = adapter
            this.layoutManager = LinearLayoutManager(context)
        }

        recommendationListViewModel.networkState
            .subscribe {
                Log.d(TAG, "PagingState = $it")
                when (it) {
                    is PagingState.InitialLoading -> {
                        progressView.visibility = View.VISIBLE
                        recommendationText.visibility = View.GONE
                    }
                    is PagingState.Loading -> {

                    }
                    is PagingState.Loaded -> {
                        progressView.visibility = View.GONE
                        recommendationText.visibility = View.VISIBLE
                    }
                    is PagingState.Error -> {
                        progressView.visibility = View.GONE
                    }
                }
            }
            .disposeBy(onDestroy)

        recommendationListViewModel.getRecommendationList(id, 1, 1)
            .subscribe {
                adapter.submitList(it)
            }
            .disposeBy(onDestroy)
    }

    private fun loadVideoData(id: Int) {
        context?.let { context ->
            movieVideoViewModel.getYouTubeVideo(context, id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ uri ->
                    playerPrepare(uri)
                    playerStart()
                }, {
                    it.printEnhancedStackTrace()
                    playerError()
                })
                .disposeBy(onDestroy)
        }
    }

    private fun playerPrepare(uri: Uri) {
        context?.let { context ->
            playManager.prepare(context, playerView, null, uri, null)
            bindPlayerListeners()
        }
    }

    private fun playerStart() {
        playManager.start()
    }

    private fun playerPause() {
        playManager.pause()
    }

    private fun playerRelease() {
        playManager.release()
    }

    private fun playerError() {
        playManager.pause()
        playerView.hideController()
        playerView.useController = false
        // TODO : error message
        playerView.exo_error_message.text = "Error"
        playerView.exo_error_message.visibility = View.VISIBLE
    }

    private fun bindPlayerListeners() {
        playManager.addAnalyticsListener(EventLogger(playManager.trackSelector))
        playManager.player?.apply {
            listenerDisposer += events().subscribe { onExoPlayerEventListener(it) }
            listenerDisposer += videos().subscribe { onExoPlayerVideoListener(it) }
        }
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
        Log.d(TAG, "onExoPlayerEventListener() event = $event")
        when (event) {
            is ExoPlayerEventPlayerStateChanged -> onPlayerStateChanged(event)
//            is ExoPlayerEventSeekProcessed -> playerStart()
            is ExoPlayerEventPlayerError -> playerError()
        }
    }

    private fun onPlayerStateChanged(event: ExoPlayerEventPlayerStateChanged) {
        Log.d(TAG, "onPlayerStateChanged() event = $event")
        when (event.playbackState) {
            Player.STATE_READY -> {
                if (!isResumed) {
                    return
                }
                hideBackdropImage()
                playerView.useController = true
                playerView.controllerHideOnTouch = true
            }
            Player.STATE_ENDED -> {
                showBackdropImage()
                playerView.controllerHideOnTouch = false
            }
        }
    }

    private fun onExoPlayerVideoListener(event: ExoPlayerVideo) {
        Log.d(TAG, "onExoPlayerVideoListener() event = $event")
        when (event) {
            is ExoPlayerVideoRenderedFirstFrame -> onRenderedFirstFrame()
        }
    }

    private fun onRenderedFirstFrame() {

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
