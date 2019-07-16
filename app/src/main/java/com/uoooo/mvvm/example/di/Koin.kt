package com.uoooo.mvvm.example.di

import com.uoooo.mvvm.example.ui.viewmodel.MovieListViewModel
import com.uoooo.mvvm.example.ui.viewmodel.MovieVideoViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { MovieListViewModel(get(), get()) }
    viewModel { MovieVideoViewModel(get(), get()) }
}
