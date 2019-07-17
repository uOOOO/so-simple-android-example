package com.uoooo.mvvm.example.ui.viewmodel.state

sealed class PagingState {
    object InitialLoading : PagingState()
    object Loading : PagingState()
    object Loaded : PagingState()
    data class Error(val error: Throwable) : PagingState()
}
