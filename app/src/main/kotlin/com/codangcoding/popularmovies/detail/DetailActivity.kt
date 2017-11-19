package com.codangcoding.popularmovies.detail

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PagerSnapHelper
import com.codangcoding.popularmovies.GlideApp
import com.codangcoding.popularmovies.NavigationController
import com.codangcoding.popularmovies.R
import com.codangcoding.popularmovies.detail.DetailUIState.UpdateType.*
import com.codangcoding.popularmovies.internal.api.ApiConstants
import com.codangcoding.popularmovies.internal.api.model.Movie
import com.codangcoding.popularmovies.internal.base.DefaultItemDecoration
import com.codangcoding.popularmovies.internal.base.LifecycleAwareActivity
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_detail.*
import javax.inject.Inject


class DetailActivity : LifecycleAwareActivity() {

    companion object {
        private val RATE_PERFECT = 9.0
        private val RATE_GOOD = 7.0
        private val RATE_NORMAL = 5.0

        private val YOUTUBE = "YouTube"
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: DetailViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(DetailViewModel::class.java)
    }
    private val clickListener: VideoClickListener = { video, _ ->
        if (YOUTUBE.equals(video.site, true)) {
            val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(String.format(ApiConstants.YOUTUBE_VIDEO_URL, video.key))
            )
            startActivity(intent)
        }
    }

    private lateinit var reviewAdapter: ReviewAdapter
    private lateinit var videoAdapter: VideoAdapter
    private lateinit var disposable: Disposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        title = null

        reviewAdapter = ReviewAdapter(this)
        rvReview.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvReview.adapter = reviewAdapter
        rvReview.itemAnimator = DefaultItemAnimator()
        rvReview.addItemDecoration(DefaultItemDecoration(this, R.dimen.item_offset))
        PagerSnapHelper().attachToRecyclerView(rvReview)

        videoAdapter = VideoAdapter(this, clickListener)
        rvVideo.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvVideo.adapter = videoAdapter
        rvVideo.itemAnimator = DefaultItemAnimator()
        rvVideo.addItemDecoration(DefaultItemDecoration(this, R.dimen.item_offset))
        PagerSnapHelper().attachToRecyclerView(rvVideo)

        val movie: Movie = intent.getParcelableExtra(NavigationController.MOVIE_EXTRA)
        viewModel.init(movie)
    }

    override fun onResume() {
        super.onResume()
        disposable = viewModel.stateChanges().subscribe { state -> updateView(state) }
        disposable = viewModel.stateChanges().subscribe { state -> updateView(state) }
    }

    override fun onPause() {
        super.onPause()
        disposable.dispose()
    }

    private fun updateView(state: DetailUIState) {
        when (state.updateType) {
            UPDATE_MOVIE -> {
                val movie = state.movie
                collapsingToolbarLayout.title = movie.title
                collapsingToolbarLayout.setExpandedTitleColor(ContextCompat.getColor(this, android.R.color.transparent))

                GlideApp.with(this)
                        .load("${ApiConstants.POSTER_BASE_URL}${movie.backdropPath}")
                        .placeholder(R.drawable.placeholder)
                        .error(R.mipmap.ic_launcher_round)
                        .into(backdropImage)

                movieOriginalTitle.text = movie.originalTitle
                movieReleaseDate.text = getString(R.string.movie_details_release_date, movie.releaseDate)

                val rate = movie.userRating
                val ratingColor = getRatingColor(rate)
                movieRating.text = getString(R.string.movie_details_rating, rate)
                movieRating.setTextColor(ratingColor)
                movieRatingText.text = getRatingText(rate)
                movieRatingText.setTextColor(ratingColor)
                movieOverview.text = movie.overview

                GlideApp.with(this)
                        .load("${ApiConstants.POSTER_BASE_URL}${movie.posterPath}")
                        .placeholder(R.drawable.placeholder)
                        .error(R.mipmap.ic_launcher_round)
                        .into(moviePoster)
            }
            UPDATE_REVIEW -> {
                reviewAdapter.setData(state.reviews)
            }
            UPDATE_VIDEO -> {
                videoAdapter.setData(state.videos)
            }
        }
    }

    private fun getRatingColor(rate: Double): Int {
        return if (rate >= RATE_PERFECT) {
            ContextCompat.getColor(this, R.color.rate_perfect)
        } else if (rate >= RATE_GOOD) {
            ContextCompat.getColor(this, R.color.rate_good)
        } else if (rate >= RATE_NORMAL) {
            ContextCompat.getColor(this, R.color.rate_normal)
        } else {
            ContextCompat.getColor(this, R.color.rate_bad)
        }
    }

    private fun getRatingText(rate: Double): String {
        return if (rate >= RATE_PERFECT) {
            getString(R.string.rate_perfect)
        } else if (rate >= RATE_GOOD) {
            getString(R.string.rate_good)
        } else if (rate >= RATE_NORMAL) {
            getString(R.string.rate_normal)
        } else {
            getString(R.string.rate_bad)
        }
    }
}