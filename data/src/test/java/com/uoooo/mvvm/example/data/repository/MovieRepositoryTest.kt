package com.uoooo.mvvm.example.data.repository

import com.uoooo.mvvm.example.data.source.MovieDataSource
import com.uoooo.mvvm.example.data.source.MovieDataSourceImpl
import com.uoooo.mvvm.example.data.source.remote.MovieDataSourceRemote
import com.uoooo.mvvm.example.data.source.remote.MovieDataSourceRemoteImpl
import com.uoooo.mvvm.example.data.source.remote.api.MovieService
import com.uoooo.mvvm.example.domain.repository.MovieRepository
import io.mockk.*
import io.reactivex.Single
import org.junit.Before
import org.junit.Test

class MovieRepositoryTest {
    private val movieService: MovieService = mockk()
    private val movieDataSourceRemote: MovieDataSourceRemote = spyk(MovieDataSourceRemoteImpl(movieService))
    private val movieDataSource: MovieDataSource = spyk(MovieDataSourceImpl(movieDataSourceRemote))
    private val movieRepository: MovieRepository = MovieRepositoryImpl(movieDataSource)

    @Before
    fun before() {
        clearMocks(movieService, movieDataSourceRemote, movieDataSource, movieService)
    }

    @Test
    fun getPopularMovie() {
        every { movieService.getPopularMovie(any()) } returns Single.just(mockk(relaxed = true))
        movieRepository.getPopular(1)
            .test()
            .await()
            .assertComplete()
        verify { movieService.getPopularMovie(any()) }
        verify { movieDataSourceRemote.getPopularMovie(any()) }
        verify { movieDataSource.getPopularMovie(any()) }
    }
}
