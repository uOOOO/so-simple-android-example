package com.uoooo.simple.example.data

import com.uoooo.simple.example.data.movie.MovieDataSourceRemote
import com.uoooo.simple.example.data.movie.MovieRepositoryImpl
import com.uoooo.simple.example.data.movie.api.MovieApi
import com.uoooo.simple.example.data.util.IODispatcher
import com.uoooo.simple.example.data.util.httpClient
import com.uoooo.simple.example.domain.Named
import com.uoooo.simple.example.domain.movie.GetMovieUseCase
import com.uoooo.simple.example.domain.movie.MovieRepository
import org.koin.dsl.module

val defaultModule = module {
    single(Named.DISPATCH_IO) { IODispatcher }
    single { httpClient }
}

val apiModule = module {
    factory { MovieApi(get()) }
}

val dataSourceModule = module {
    factory { MovieDataSourceRemote(get()) }
}

val repositoryModule = module {
    factory<MovieRepository> { MovieRepositoryImpl(get()) }
}
