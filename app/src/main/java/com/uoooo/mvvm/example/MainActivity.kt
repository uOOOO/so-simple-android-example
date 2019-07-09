package com.uoooo.mvvm.example

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakewharton.rxbinding3.swiperefreshlayout.refreshes
import com.uoooo.mvvm.example.domain.model.Movie
import com.uoooo.mvvm.example.ui.movie.PopularMovieAdapter
import com.uoooo.mvvm.example.ui.viewmodel.MovieViewModel
import io.reactivex.subjects.PublishSubject
import io.sellmair.disposer.disposeBy
import io.sellmair.disposer.onDestroy
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private val movieViewModel: MovieViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val itemClickObserver = PublishSubject.create<Movie>().apply {
            subscribe {}
            .disposeBy(onDestroy)
        }

        val adapter = PopularMovieAdapter(itemClickObserver)

        movieList.apply {
            setHasFixedSize(true)
            this.adapter = adapter
            this.layoutManager = LinearLayoutManager(context)
        }

        movieListSwipeRefresh.refreshes()
            .subscribe {
                adapter.currentList?.dataSource?.invalidate()
            }
            .disposeBy(onDestroy)

        movieViewModel.getPopularMovieList()
            .subscribe {
                adapter.submitList(it)
                movieListSwipeRefresh.isRefreshing = false
            }
            .disposeBy(onDestroy)
    }
}
