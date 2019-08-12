package com.uoooo.simple.example.data.di

import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.EnumJsonAdapter
import com.uoooo.simple.example.data.BuildConfig
import com.uoooo.simple.example.data.ServerConfig
import com.uoooo.simple.example.data.entity.Video
import com.uoooo.simple.example.data.misc.ApiKeyInterceptor
import com.uoooo.simple.example.data.repository.MovieRepositoryImpl
import com.uoooo.simple.example.data.source.MovieDataSource
import com.uoooo.simple.example.data.source.MovieDataSourceImpl
import com.uoooo.simple.example.data.source.remote.MovieDataSourceRemote
import com.uoooo.simple.example.data.source.remote.MovieDataSourceRemoteImpl
import com.uoooo.simple.example.data.source.remote.api.MovieService
import com.uoooo.simple.example.domain.repository.MovieRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

val webServiceModule = module {
    single {
        OkHttpClient.Builder()
            .addInterceptor(ApiKeyInterceptor())
            .addInterceptor(ChuckerInterceptor(get()))
            .addInterceptor(HttpLoggingInterceptor().apply {
                level =
                    if (BuildConfig.DEBUG)
                        HttpLoggingInterceptor.Level.BODY
                    else
                        HttpLoggingInterceptor.Level.NONE
            })
            .build()
    }
    single {
        RxJava2CallAdapterFactory.create()
    }
    single {
        val moshi = Moshi.Builder()
            .add(
                Video.Type::class.java,
                EnumJsonAdapter.create(Video.Type::class.java).withUnknownFallback(Video.Type.UNKNOWN)
            )
            .add(
                Video.Site::class.java,
                EnumJsonAdapter.create(Video.Site::class.java).withUnknownFallback(Video.Site.UNKNOWN)
            )
            .build()
        MoshiConverterFactory.create(moshi)
    }
    single {
        Retrofit.Builder()
            .baseUrl(ServerConfig.API_BASE_URL)
            .client(get())
            .addCallAdapterFactory(get<RxJava2CallAdapterFactory>())
            .addConverterFactory(get<MoshiConverterFactory>())
            .build()
            .create(MovieService::class.java)
    }
}

val dataSourceModule = module {
    factory<MovieDataSourceRemote> { MovieDataSourceRemoteImpl(get()) }
    factory<MovieDataSource> { MovieDataSourceImpl(get()) }
}

val repositoryModule = module {
    factory<MovieRepository> { MovieRepositoryImpl(get()) }
}