package com.uoooo.simple.example.domain

import com.uoooo.simple.example.domain.movie.GetMovieUseCase
import org.koin.core.qualifier.named
import org.koin.dsl.module

object Named {
    val DISPATCH_IO = named("IO")
}

val useCaseModule = module {
    factory { GetMovieUseCase() }
}
