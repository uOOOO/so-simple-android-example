package com.uoooo.simple.example.ui.viewmodel.state

sealed class PagingState {
    object InitialLoading : PagingState()
    object Loading : PagingState()
    data class Loaded(val isEmptyResult: Boolean) : PagingState()
    data class Error(val error: Throwable) : PagingState()
}
