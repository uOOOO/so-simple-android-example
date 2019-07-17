package com.uoooo.simple.example.domain.di

import com.uoooo.simple.example.domain.interactor.GetMovieUseCase
import com.uoooo.simple.example.domain.interactor.GetMovieUseCaseImpl
import org.koin.dsl.module

val interactorModule = module {
    factory<GetMovieUseCase> { GetMovieUseCaseImpl(get()) }
}
