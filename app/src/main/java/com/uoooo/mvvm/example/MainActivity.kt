package com.uoooo.mvvm.example

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakewharton.rxbinding3.swiperefreshlayout.refreshes
import com.uoooo.mvvm.example.domain.model.Movie
import com.uoooo.mvvm.example.ui.detail.DetailFragment
import com.uoooo.mvvm.example.ui.movie.PopularMovieAdapter
import com.uoooo.mvvm.example.ui.viewmodel.MovieViewModel
import io.reactivex.subjects.PublishSubject
import io.sellmair.disposer.disposeBy
import io.sellmair.disposer.onDestroy
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.ext.getFullName

class MainActivity : AppCompatActivity() {
    private val movieViewModel: MovieViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val itemClickObserver = PublishSubject.create<Movie>().apply {
            subscribe {
                supportFragmentManager.beginTransaction()
                    .add(R.id.fragmentContainer, DetailFragment.newInstance(it), DetailFragment::class.getFullName())
                    .commitAllowingStateLoss()
            }.disposeBy(onDestroy)
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

//        supportFragmentManager.beginTransaction()
//            .add(R.id.fragmentContainer, DetailFragment(), DetailFragment::class.getFullName())
//            .commitAllowingStateLoss()
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
