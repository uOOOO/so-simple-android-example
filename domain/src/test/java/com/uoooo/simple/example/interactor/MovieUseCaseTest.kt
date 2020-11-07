package com.uoooo.simple.example.interactor

import com.uoooo.simple.example.domain.interactor.MovieUseCase
import com.uoooo.simple.example.domain.repository.MovieRepository
import io.mockk.*
import io.reactivex.Single
import org.junit.Before
import org.junit.Test

class MovieUseCaseTest {
    private val movieRepository: MovieRepository = mockk()
    private var movieUseCase: MovieUseCase = MovieUseCase(movieRepository)

    @Before
    fun before() {
        clearMocks(movieRepository)
    }

    @Test
    fun getPopularMovie() {
        every { movieRepository.getPopular(any()) } returns Single.just(mockk(relaxed = true))
        movieUseCase.getPopularMovie(1)
            .test()
            .await()
            .assertComplete()
        verify { movieRepository.getPopular(any()) }
    }
}
