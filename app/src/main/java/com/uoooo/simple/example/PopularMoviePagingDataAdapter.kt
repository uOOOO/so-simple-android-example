package com.uoooo.simple.example

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.jakewharton.rxbinding4.view.clicks
import com.uoooo.simple.example.data.ServerConfig
import com.uoooo.simple.example.domain.model.Movie
import com.uoooo.simple.example.ui.common.getPosterImageUrl
import io.reactivex.rxjava3.core.Observer
import kotlinx.android.synthetic.main.recyclerview_item_popular_movie.view.*

class PopularMoviePagingDataAdapter constructor(
    diffCallback: DiffUtil.ItemCallback<Movie>,
    private val itemClickObserver: Observer<Movie>? = null
) : PagingDataAdapter<Movie, PopularMoviePagingDataAdapter.PopularMovieViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularMovieViewHolder {
        return PopularMovieViewHolder(parent)
    }

    override fun onBindViewHolder(holder: PopularMovieViewHolder, position: Int) {
        holder.bind(getItem(position), itemClickObserver)
    }

    class PopularMovieViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_item_popular_movie, parent, false)
    ) {
        private val itemRootLayout = itemView.itemRootLayout
        private val posterImage = itemView.posterImage
        private val titleText = itemView.titleText
        private val voteAverageText = itemView.voteAverageText
        private val releaseDateText = itemView.releaseDateText

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
                voteAverageText.text = String.format("%.1f", movie.voteAverage)

                itemClickObserver?.apply {
                    itemRootLayout.clicks()
                        .map { movie }
                        .subscribe(this)
                }
            }
        }
    }
}