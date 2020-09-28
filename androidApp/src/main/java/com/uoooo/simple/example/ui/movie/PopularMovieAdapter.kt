package com.uoooo.simple.example.ui.movie

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.jakewharton.rxbinding3.view.clicks
import com.uoooo.simple.example.GlideApp
import com.uoooo.simple.example.R
import com.uoooo.simple.example.data.ServerConfig
import com.uoooo.simple.example.domain.model.Movie
import com.uoooo.simple.example.ui.common.getPosterImageUrl
import io.reactivex.Observer
import kotlinx.android.synthetic.main.recyclerview_item_popular_movie.view.*

class PopularMovieAdapter(private val itemClickObserver: Observer<Movie>?) :
    PagedListAdapter<Movie, PopularMovieAdapter.PopularMovieViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularMovieViewHolder {
        return PopularMovieViewHolder(parent)
    }

    override fun onBindViewHolder(holder: PopularMovieViewHolder, position: Int) {
        getItem(position)?.run {
            holder.bind(this, itemClickObserver)
        }
    }

    class PopularMovieViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.recyclerview_item_popular_movie,
            parent,
            false
        )
    ) {
        private val itemRootLayout = itemView.itemRootLayout
        private val posterImage = itemView.posterImage
        private val titleText = itemView.titleText
        private val voteAverageText = itemView.voteAverageText
        private val releaseDateText = itemView.releaseDateText

        fun bind(movie: Movie, itemClickObserver: Observer<Movie>?) {
            movie.run {
                GlideApp.with(posterImage)
                    .load(getPosterImageUrl(posterPath, ServerConfig.ImageSize.NORMAL))
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(posterImage)

                titleText.text = title
                releaseDateText.text = releaseDate
                voteAverageText.text = String.format("%.1f", voteAverage)

                itemClickObserver?.apply {
                    itemRootLayout.clicks()
                        .map { movie }
                        .subscribe(this)
                }
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Movie>() {
            override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem == newItem
            }
        }
    }
}
