package com.uoooo.simple.example

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.jakewharton.rxbinding4.swiperefreshlayout.refreshes
import com.uoooo.simple.example.domain.model.Movie
import com.uoooo.simple.example.ui.detail.DetailFragment
import com.uoooo.simple.example.repo.PopularMovieDiffCallback
import com.uoooo.simple.example.ui.viewmodel.PopularMovieViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.subjects.PublishSubject
import io.sellmair.disposer.disposeBy
import io.sellmair.disposer.onDestroy
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.rx3.asObservable

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val popularMovieViewModel: PopularMovieViewModel by viewModels()

    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initMovieList()
        popularMovieViewModel.loadPopularMovie(1, 6)

//        supportFragmentManager.beginTransaction()
//            .add(R.id.fragmentContainer, DetailFragment(), DetailFragment::class.getFullName())
//            .commitAllowingStateLoss()
    }

    @ExperimentalCoroutinesApi
    private fun initMovieList() {
        val itemClickObserver = PublishSubject.create<Movie>().apply {
            subscribe {
                supportFragmentManager.beginTransaction()
                    .replace(
                        R.id.fragmentContainer,
                        DetailFragment.newInstance(it),
                        DetailFragment::class.qualifiedName
                    )
                    .commitAllowingStateLoss()
            }.disposeBy(onDestroy)
        }

        val pagingDataAdapter =
            PopularMoviePagingDataAdapter(PopularMovieDiffCallback(), itemClickObserver)

        pagingDataAdapter.loadStateFlow.asObservable()
            .subscribe {
                when (it.refresh) {
                    is LoadState.Error, is LoadState.NotLoading ->
                        progressView.visibility = View.GONE
                    is LoadState.Loading ->
                        progressView.visibility = View.VISIBLE
                }
            }
            .disposeBy(onDestroy)

        movieList.apply {
            setHasFixedSize(true)
            this.adapter = pagingDataAdapter
            this.layoutManager = GridLayoutManager(context, 3)
        }

        movieListSwipeRefresh.refreshes()
            .subscribe {
                popularMovieViewModel.invalidatePopularMovie()
            }
            .disposeBy(onDestroy)

        popularMovieViewModel.popularMovieList
            .subscribe {
                pagingDataAdapter.submitData(lifecycle, it)
                movieListSwipeRefresh.isRefreshing = false
            }
            .disposeBy(onDestroy)
    }

    override fun onDestroy() {
        movieList.adapter = null
        super.onDestroy()
    }

    override fun onBackPressed() {
        supportFragmentManager.findFragmentByTag(DetailFragment::class.qualifiedName)?.run {
            Log.d(TAG, "onBackPressed : $this")
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
