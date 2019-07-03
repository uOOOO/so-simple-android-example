package com.uoooo.mvvm.example.di

import com.uoooo.mvvm.example.ui.viewmodel.MovieViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { MovieViewModel(get(), get()) }
}
