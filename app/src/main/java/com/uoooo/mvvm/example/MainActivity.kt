package com.uoooo.mvvm.example

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakewharton.rxbinding3.swiperefreshlayout.refreshes
import com.uber.autodispose.AutoDispose.autoDisposable
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.uoooo.mvvm.example.domain.model.Movie
import com.uoooo.mvvm.example.ui.movie.PopularMovieAdapter
import com.uoooo.mvvm.example.ui.viewmodel.MovieViewModel
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private val movieViewModel: MovieViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val itemClickObserver = PublishSubject.create<Movie>().apply {
            `as`(autoDisposable(AndroidLifecycleScopeProvider.from(this@MainActivity, Lifecycle.Event.ON_DESTROY)))
                .subscribe {}
        }

        val adapter = PopularMovieAdapter(itemClickObserver)

        movieList.apply {
            setHasFixedSize(true)
            this.adapter = adapter
            this.layoutManager = LinearLayoutManager(context)
        }

        movieListSwipeRefresh.refreshes()
            .`as`(autoDisposable(AndroidLifecycleScopeProvider.from(this, Lifecycle.Event.ON_DESTROY)))
            .subscribe {
                adapter.currentList?.dataSource?.invalidate()
            }

        movieViewModel.getPopularMovieList()
            .`as`(autoDisposable(AndroidLifecycleScopeProvider.from(this, Lifecycle.Event.ON_DESTROY)))
            .subscribe {
                adapter.submitList(it)
                movieListSwipeRefresh.isRefreshing = false
            }
    }
}
