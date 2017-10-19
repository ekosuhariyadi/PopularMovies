package com.codangcoding.popularmovies

import android.app.Activity
import android.content.Intent
import android.support.v4.app.ActivityOptionsCompat
import android.view.View
import com.codangcoding.popularmovies.detail.DetailActivity
import com.codangcoding.popularmovies.internal.api.model.Movie
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class NavigationController @Inject constructor() {

    companion object {
        const val MOVIE_EXTRA = "movie"
    }

    fun navigateToDetail(sourceActivity: Activity, movie: Movie, view: View) {
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                sourceActivity, view, sourceActivity.getString(R.string.shared_image))
        val intent = Intent(sourceActivity, DetailActivity::class.java)
                .apply { putExtra(MOVIE_EXTRA, movie) }
        sourceActivity.startActivity(intent, options.toBundle())
    }
}