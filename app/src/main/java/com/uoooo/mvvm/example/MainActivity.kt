package com.uoooo.mvvm.example

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.uoooo.mvvm.example.domain.interactor.GetMovieUseCase
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {
    private val useCase: GetMovieUseCase by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        useCase.getPopularMovie(1)
            .subscribeOn(Schedulers.io())
            .subscribe({ println(it) }, { println(it) })
    }
}
