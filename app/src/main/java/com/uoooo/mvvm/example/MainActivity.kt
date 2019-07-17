package com.uoooo.mvvm.example

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakewharton.rxbinding3.swiperefreshlayout.refreshes
import com.uoooo.mvvm.example.domain.model.Movie
import com.uoooo.mvvm.example.ui.detail.DetailFragment
import com.uoooo.mvvm.example.ui.movie.MovieAdapter
import com.uoooo.mvvm.example.ui.viewmodel.PopularListViewModel
import com.uoooo.mvvm.example.ui.viewmodel.state.PagingState
import io.reactivex.subjects.PublishSubject
import io.sellmair.disposer.disposeBy
import io.sellmair.disposer.onDestroy
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.ext.getFullName

class MainActivity : AppCompatActivity() {
    private val movieListViewModel: PopularListViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initMovieList()

//        supportFragmentManager.beginTransaction()
//            .add(R.id.fragmentContainer, DetailFragment(), DetailFragment::class.getFullName())
//            .commitAllowingStateLoss()
    }

    private fun initMovieList() {
        val itemClickObserver = PublishSubject.create<Movie>().apply {
            subscribe {
                supportFragmentManager.beginTransaction()
                    .add(R.id.fragmentContainer, DetailFragment.newInstance(it), DetailFragment::class.getFullName())
                    .commitAllowingStateLoss()
            }.disposeBy(onDestroy)
        }

        val adapter = MovieAdapter(itemClickObserver)

        movieList.apply {
            setHasFixedSize(true)
            this.adapter = adapter
            this.layoutManager = LinearLayoutManager(context)
        }

        movieListSwipeRefresh.refreshes()
            .subscribe {
                movieListViewModel.invalidate(adapter)
            }
            .disposeBy(onDestroy)

        movieListViewModel.networkState
            .subscribe {
                Log.d(TAG, "PagingState = $it")
                when (it) {
                    is PagingState.InitialLoading -> {
                        progressView.visibility = View.VISIBLE
                    }
                    is PagingState.Loading -> {

                    }
                    is PagingState.Loaded -> {
                        progressView.visibility = View.GONE
                    }
                    is PagingState.Error -> {
                        progressView.visibility = View.GONE
                    }
                }
            }
            .disposeBy(onDestroy)

        movieListViewModel.getPopularList(1, 5)
            .subscribe {
                adapter.submitList(it)
                movieListSwipeRefresh.isRefreshing = false
            }
            .disposeBy(onDestroy)
    }

    override fun onBackPressed() {
        supportFragmentManager.findFragmentByTag(DetailFragment::class.getFullName())?.run {
            Log.d(TAG, "onBackPressed : ${this}")
            supportFragmentManager.beginTransaction()
                .remove(this@run)
                .commitAllowingStateLoss()
            return
        }
        super.onBackPressed()
    }

    companion object {
        private val TAG: String = MainActivity::class.java.simpleName
    }
}
