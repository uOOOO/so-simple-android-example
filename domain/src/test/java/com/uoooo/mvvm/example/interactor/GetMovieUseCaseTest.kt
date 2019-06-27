package com.uoooo.mvvm.example.interactor

import com.uoooo.mvvm.example.domain.interactor.GetMovieUseCase
import com.uoooo.mvvm.example.domain.interactor.GetMovieUseCaseImpl
import com.uoooo.mvvm.example.domain.repository.MovieRepository
import io.mockk.*
import io.reactivex.Single
import org.junit.Before
import org.junit.Test

class GetMovieUseCaseTest {
    private val movieRepository: MovieRepository = mockk()
    private var getMovieUseCase: GetMovieUseCase = GetMovieUseCaseImpl(movieRepository)

    @Before
    fun before() {
        clearMocks(movieRepository)
    }

    @Test
    fun getPopularMovie() {
        every { movieRepository.getPopularMovie(any()) } returns Single.just(mockk(relaxed = true))
        getMovieUseCase.getPopularMovie(1)
            .test()
            .await()
            .assertComplete()
        verify { movieRepository.getPopularMovie(any()) }
    }
}
