package com.uoooo.mvvm.example.domain.di

import com.uoooo.mvvm.example.domain.interactor.GetMovieUseCase
import com.uoooo.mvvm.example.domain.interactor.GetMovieUseCaseImpl
import org.koin.dsl.module

val interactorModule = module {
    factory<GetMovieUseCase> { GetMovieUseCaseImpl(get()) }
}
