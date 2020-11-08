package com.uoooo.simple.example.data.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.EnumJsonAdapter
import com.uoooo.simple.example.data.BuildConfig
import com.uoooo.simple.example.data.ServerConfig
import com.uoooo.simple.example.data.entity.Video
import com.uoooo.simple.example.data.misc.ApiKeyInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object HttpModule {
    @Singleton
    @Provides
    fun provideOkHttpClient(@ApplicationContext appContext: Context): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(ApiKeyInterceptor())
            .addInterceptor(ChuckerInterceptor(appContext))
            .addInterceptor(HttpLoggingInterceptor()
                .apply {
                    level =
                        if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
                        else HttpLoggingInterceptor.Level.NONE
                })
            .build()

    @Singleton
    @Provides
    fun provideCallAdapterFactory(): CallAdapter.Factory = RxJava3CallAdapterFactory.create()

    @Singleton
    @Provides
    fun provideConvertFactory(): Converter.Factory = MoshiConverterFactory.create(
        Moshi.Builder()
            .add(
                Video.Type::class.java,
                EnumJsonAdapter.create(Video.Type::class.java)
                    .withUnknownFallback(Video.Type.UNKNOWN)
            )
            .add(
                Video.Site::class.java,
                EnumJsonAdapter.create(Video.Site::class.java)
                    .withUnknownFallback(Video.Site.UNKNOWN)
            )
            .build()
    )

    @Singleton
    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        callAdapterFactory: CallAdapter.Factory,
        converterFactory: Converter.Factory
    ): Retrofit = Retrofit.Builder()
        .baseUrl(ServerConfig.API_BASE_URL)
        .client(okHttpClient)
        .addCallAdapterFactory(callAdapterFactory)
        .addConverterFactory(converterFactory)
        .build()
}
