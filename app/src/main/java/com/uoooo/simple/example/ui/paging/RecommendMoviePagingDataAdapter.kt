package com.uoooo.simple.example.ui.paging

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxbinding4.view.clicks
import com.uoooo.simple.example.databinding.RecyclerviewItemRecommendMovieBinding
import com.uoooo.simple.example.domain.model.Movie
import io.reactivex.rxjava3.core.Observer

class RecommendMoviePagingDataAdapter constructor(
    diffCallback: DiffUtil.ItemCallback<Movie>,
    private val itemClickObserver: Observer<Movie>? = null
) : PagingDataAdapter<Movie, RecommendMoviePagingDataAdapter.RecommendMovieViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendMovieViewHolder {
        return RecommendMovieViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: RecommendMovieViewHolder, position: Int) {
        holder.bind(getItem(position), itemClickObserver)
    }

    class RecommendMovieViewHolder private constructor(
        private val binding: RecyclerviewItemRecommendMovieBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: Movie?, itemClickObserver: Observer<Movie>?) {
            if (movie == null) {
                itemView.visibility = View.GONE
            } else {
                itemView.visibility = View.VISIBLE
                binding.movie = movie

                itemClickObserver?.apply {
                    binding.root.clicks()
                        .map { movie }
                        .subscribe(this)
                }
            }
        }

        companion object {
            fun create(parent: ViewGroup): RecommendMovieViewHolder {
                return RecommendMovieViewHolder(
                    RecyclerviewItemRecommendMovieBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    )
                )
            }
        }
    }
}