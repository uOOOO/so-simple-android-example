package com.uoooo.simple.example.ui.movie.repository.model

sealed class LoadingState {
    object InitialLoading : LoadingState()
    object Loading : LoadingState()
    data class Loaded(val isEmptyResult: Boolean) : LoadingState()
    data class Error(val error: Throwable, val `when`: LoadingState) : LoadingState()
}
