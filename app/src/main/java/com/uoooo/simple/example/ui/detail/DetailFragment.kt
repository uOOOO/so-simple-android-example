package com.uoooo.simple.example.ui.detail

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.util.Pair
import android.view.LayoutInflater
import android.view.SurfaceView
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionLayout
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
import com.uoooo.simple.example.ui.movie.RecommendMovieAdapter
import com.uoooo.simple.example.ui.movie.repository.model.LoadingState
import com.uoooo.simple.example.ui.player.ExoPlayerPlayManager
import com.uoooo.simple.example.ui.player.rx.*
import com.uoooo.simple.example.ui.viewmodel.RecommendMovieViewModel
import com.uoooo.simple.example.ui.viewmodel.VideoViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject
import io.sellmair.disposer.Disposer
import io.sellmair.disposer.disposeBy
import io.sellmair.disposer.onDestroy
import kotlinx.android.synthetic.main.exo_simple_player_view.view.*
import kotlinx.android.synthetic.main.layout_detail.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.ext.getFullName

class DetailFragment : Fragment(), MotionLayout.TransitionListener {
    override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {

    }

    override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {
        if (p1 == p0?.endState &&
            p2 == p0.startState
        ) {
            playerView.hideController()
        }
    }

    override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {
        var value = p3
        if (value > 1) {
            value = 1f
        } else if (value < 0) {
            value = 0f
        }
        value = 1 - value
        recommendationList.alpha = value
        recommendationText.alpha = value

        if (p1 == p0?.startState &&
            p2 == p0.endState &&
            p3 > 0.05f
        ) {
            playerView.hideController()
        }
    }

    override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
        if (p1 == p0?.startState) {
            playerView.showController()
        }
    }

    private val recommendMovieViewModel: RecommendMovieViewModel by viewModel()
    private val videoViewModel: VideoViewModel by viewModel()
    private val playManager: ExoPlayerPlayManager by lazy {
        ExoPlayerPlayManager()
    }
    private val listenerDisposer = Disposer.create(onDestroy)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (arguments?.getSerializable(BUNDLE_OBJECT) as Movie?)?.let { movie ->
            initView(movie)
            loadVideo(movie.id)
            loadRecommendation(movie.id)
        }
    }

    private fun initView(movie: Movie) {
        motionRootView.setTransitionListener(this)
        initVideo(movie.backdropPath)
        initRecommendationList()
    }

    private fun initVideo(backdropPath: String?) {
        GlideApp.with(playerView.exo_backdrop)
            .load(getPosterImageUrl(backdropPath, ServerConfig.ImageSize.NORMAL))
            .transition(DrawableTransitionOptions.withCrossFade())
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(playerView.exo_backdrop)

        playerView.useController = true
        playerView.controllerShowTimeoutMs = 1500 * 3
        // TODO : error message
        playerView.setErrorMessageProvider { Pair.create(-1, "Error") }
    }

    private fun initRecommendationList() {
        val itemClickObserver = PublishSubject.create<Movie>().apply {
            subscribe { movie ->
                (view?.parent as ViewGroup?)?.id?.let { containerId ->
                    fragmentManager?.run {
                        this.beginTransaction()
                            .replace(
                                containerId,
                                newInstance(movie),
                                DetailFragment::class.getFullName()
                            )
                            .commit()
                    }
                }
            }.disposeBy(onDestroy)
        }

        val adapter = RecommendMovieAdapter(itemClickObserver)

        recommendationList.apply {
            setHasFixedSize(true)
            this.adapter = adapter
            this.layoutManager = LinearLayoutManager(context)
        }

        recommendMovieViewModel.getLoadingState()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                Log.d(TAG, "LoadingState = $it")
                when (it) {
                    is LoadingState.InitialLoading -> {
                        progressView.visibility = View.VISIBLE
                        recommendationText.visibility = View.GONE
                    }
                    is LoadingState.Loading -> {

                    }
                    is LoadingState.Loaded -> {
                        progressView.visibility = View.GONE
                        recommendationText.visibility =
                            if (adapter.itemCount == 0 && it.isEmptyResult) View.GONE else View.VISIBLE
                    }
                    is LoadingState.Error -> {
                        progressView.visibility = View.GONE
                    }
                }
            }
            .disposeBy(onDestroy)

        recommendMovieViewModel.getPagedList()
            .subscribe {
                adapter.submitList(it)
            }
            .disposeBy(onDestroy)
    }

    private fun loadVideo(id: Int) {
        videoViewModel.getYouTubeVideo(id)
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

    private fun loadRecommendation(id: Int) {
        recommendMovieViewModel.loadRecommendationList(id, 1, 1)
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
        // TODO : handle player nullable more properly
        listenerDisposer += playManager.getEventListener()?.subscribe { onExoPlayerEventListener(it) }
            ?: return
        listenerDisposer += playManager.getVideoListener()?.subscribe { onExoPlayerVideoListener(it) }
            ?: return
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
            is ExoPlayerVideoSurfaceSizeChanged -> onSurfaceSizeChanged(event)
        }
    }

    private fun onRenderedFirstFrame() {

    }

    private fun onSurfaceSizeChanged(event: ExoPlayerVideoSurfaceSizeChanged) {
        val surfaceView = playerView.videoSurfaceView
        if (surfaceView is SurfaceView) {
            surfaceView.holder.setFixedSize(event.width, event.height)
        }
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
