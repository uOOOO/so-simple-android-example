package com.uoooo.simple.example.di

import com.uoooo.simple.example.ui.viewmodel.PopularMovieViewModel
import com.uoooo.simple.example.ui.viewmodel.RecommendMovieViewModel
import com.uoooo.simple.example.ui.viewmodel.VideoViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { PopularMovieViewModel(get(), get()) }
    viewModel { RecommendMovieViewModel(get(), get()) }
    viewModel { VideoViewModel(get(), get()) }
}
