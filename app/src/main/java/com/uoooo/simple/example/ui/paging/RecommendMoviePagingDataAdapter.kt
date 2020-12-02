package com.uoooo.simple.example.ui.paging

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.jakewharton.rxbinding4.view.clicks
import com.uoooo.simple.example.GlideApp
import com.uoooo.simple.example.R
import com.uoooo.simple.example.data.ServerConfig
import com.uoooo.simple.example.domain.model.Movie
import com.uoooo.simple.example.ui.common.getPosterImageUrl
import io.reactivex.rxjava3.core.Observer
import kotlinx.android.synthetic.main.recyclerview_item_recommend_movie.view.*

class RecommendMoviePagingDataAdapter constructor(
    diffCallback: DiffUtil.ItemCallback<Movie>,
    private val itemClickObserver: Observer<Movie>? = null
) : PagingDataAdapter<Movie, RecommendMoviePagingDataAdapter.RecommendMovieViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendMovieViewHolder {
        return RecommendMovieViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecommendMovieViewHolder, position: Int) {
        holder.bind(getItem(position), itemClickObserver)
    }

    class RecommendMovieViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_item_recommend_movie, parent, false)
    ) {
        private val itemRootLayout = itemView.itemRootLayout
        private val posterImage = itemView.posterImage
        private val voteAverageProgress = itemView.voteAverageProgress
        private val voteAverageText = itemView.voteAverageText
        private val titleText = itemView.titleText
        private val releaseDateText = itemView.releaseDateText
        private val overviewText = itemView.overviewText

        fun bind(movie: Movie?, itemClickObserver: Observer<Movie>?) {
            if (movie == null) {
                itemView.visibility = View.GONE
            } else {
                itemView.visibility = View.VISIBLE
                GlideApp.with(posterImage)
                    .load(getPosterImageUrl(movie.posterPath, ServerConfig.ImageSize.NORMAL))
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(posterImage)

                titleText.text = movie.title
                releaseDateText.text = movie.releaseDate
                overviewText.text = movie.overview

                (movie.voteAverage * 10).toInt().run {
                    voteAverageProgress.progress = this
                    voteAverageText.text = String.format("%d%%", this)
                }

                itemClickObserver?.apply {
                    itemRootLayout.clicks()
                        .map { movie }
                        .subscribe(this)
                }
            }
        }
    }
}