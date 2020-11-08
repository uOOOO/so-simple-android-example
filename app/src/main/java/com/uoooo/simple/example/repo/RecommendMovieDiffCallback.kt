package com.uoooo.simple.example.repo

import androidx.recyclerview.widget.DiffUtil
import com.uoooo.simple.example.domain.model.Movie

class RecommendMovieDiffCallback : DiffUtil.ItemCallback<Movie>() {
    override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem == newItem
    }
}