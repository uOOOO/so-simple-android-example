package com.uoooo.mvvm.example.di

import com.uoooo.mvvm.example.ui.viewmodel.MovieVideoViewModel
import com.uoooo.mvvm.example.ui.viewmodel.PopularListViewModel
import com.uoooo.mvvm.example.ui.viewmodel.RecommendationListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { PopularListViewModel(get(), get()) }
    viewModel { RecommendationListViewModel(get(), get()) }
    viewModel { MovieVideoViewModel(get(), get()) }
}
