package com.codangcoding.popularmovies.main

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import com.codangcoding.popularmovies.NavigationController
import com.codangcoding.popularmovies.R
import com.codangcoding.popularmovies.internal.api.MovieDbService
import com.codangcoding.popularmovies.internal.base.LifecycleAwareActivity
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject


class MainActivity : LifecycleAwareActivity() {

    @Inject
    lateinit var navigationController: NavigationController
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var movieDbService: MovieDbService

    private val viewModel: MainViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)
    }
    private val clickListener: MovieClickListener = { movie, view ->
        navigationController.navigateToDetail(this@MainActivity, movie, view)
    }
    private val mainAdapter: MainAdapter by lazy { MainAdapter(this, clickListener) }
    private lateinit var disposable: Disposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        movieList.run {
            adapter = mainAdapter
            layoutManager = GridLayoutManager(this@MainActivity, resources.getInteger(R.integer.grid_column))
        }

        viewModel.loadPopularMovies()
    }

    override fun onResume() {
        super.onResume()
        disposable = viewModel.stateChanges().subscribe { state -> updateView(state) }
    }

    override fun onPause() {
        super.onPause()
        disposable.dispose()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_popular -> {
                viewModel.loadPopularMovies()
                true
            }
            R.id.menu_toprated -> {
                viewModel.loadTopRatedMovies()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun updateView(state: MainUIState) {
        state.run {
            if (error.isNotEmpty()) {
                Toast.makeText(
                        this@MainActivity,
                        getString(R.string.network_request_error),
                        Toast.LENGTH_SHORT
                ).show();
            } else {
                loadingIndicator.visibility = if (isLoading) VISIBLE else GONE
                if (!isLoading)
                    mainAdapter.setData(state.movies)
            }
        }
    }
}