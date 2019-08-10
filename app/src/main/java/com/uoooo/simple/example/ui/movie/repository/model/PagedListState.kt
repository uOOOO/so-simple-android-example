package com.uoooo.simple.example.ui.movie.repository.model

import androidx.paging.PagedList
import io.reactivex.Observable

data class PagedListState<T>(
    val pagedList: Observable<PagedList<T>>,
    val loadingState: Observable<LoadingState>,
    val invalidate: () -> Unit
)
