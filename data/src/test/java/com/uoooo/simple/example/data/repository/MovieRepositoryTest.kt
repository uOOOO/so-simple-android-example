package com.uoooo.simple.example.data.repository

import com.uoooo.simple.example.data.source.remote.MovieDataSourceRemote
import com.uoooo.simple.example.data.source.remote.api.MovieService
import com.uoooo.simple.example.domain.repository.MovieRepository
import io.mockk.*
import io.reactivex.Single
import org.junit.Before
import org.junit.Test

class MovieRepositoryTest {
    private val movieService: MovieService = mockk()
    private val movieDataSourceRemote: MovieDataSourceRemote = spyk(MovieDataSourceRemote(movieService))
    private val movieRepository: MovieRepository = MovieRepositoryImpl(movieDataSourceRemote)

    @Before
    fun before() {
        clearMocks(movieService, movieDataSourceRemote, movieService)
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
    }
}
