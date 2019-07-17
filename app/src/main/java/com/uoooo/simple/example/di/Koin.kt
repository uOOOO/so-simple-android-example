package com.uoooo.simple.example.di

import com.uoooo.simple.example.ui.viewmodel.MovieVideoViewModel
import com.uoooo.simple.example.ui.viewmodel.PopularListViewModel
import com.uoooo.simple.example.ui.viewmodel.RecommendationListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { PopularListViewModel(get(), get()) }
    viewModel { RecommendationListViewModel(get(), get()) }
    viewModel { MovieVideoViewModel(get(), get()) }
}
