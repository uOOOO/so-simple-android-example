package com.uoooo.simple.example.ui

import android.widget.ImageView
import android.widget.ProgressBar
import androidx.databinding.BindingAdapter
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.uoooo.simple.example.GlideApp
import com.uoooo.simple.example.data.ServerConfig
import com.uoooo.simple.example.domain.model.Movie
import com.uoooo.simple.example.ui.common.getPosterImageUrl

@BindingAdapter("android:src")
fun src(imageView: ImageView, movie: Movie) {
    GlideApp.with(imageView)
        .load(getPosterImageUrl(movie.posterPath, ServerConfig.ImageSize.NORMAL))
        .transition(DrawableTransitionOptions.withCrossFade())
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .into(imageView)
}

@BindingAdapter("android:progress")
fun progress(progressBar: ProgressBar, movie: Movie) {
    progressBar.progress = (movie.voteAverage * 10).toInt()
}
