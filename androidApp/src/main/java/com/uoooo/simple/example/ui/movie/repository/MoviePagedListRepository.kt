package com.uoooo.simple.example.ui.movie.repository

import com.uoooo.simple.example.domain.model.Movie
import com.uoooo.simple.example.ui.common.BaseRepository
import com.uoooo.simple.example.ui.movie.repository.model.PagedListState

interface MoviePagedListRepository : BaseRepository {
    fun getPopular(startPage: Int, endPage: Int): PagedListState<Movie>
    fun getRecommend(id: Int, startPage: Int, endPage: Int): PagedListState<Movie>
}
