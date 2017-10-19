package com.codangcoding.popularmovies.detail

import android.arch.lifecycle.ViewModel
import com.codangcoding.elmandroid.AbstractCmd.Cmd
import com.codangcoding.elmandroid.AbstractCmd.None
import com.codangcoding.elmandroid.AbstractMsg.Idle
import com.codangcoding.elmandroid.AbstractMsg.Msg
import com.codangcoding.elmandroid.Program
import com.codangcoding.elmandroid.State
import com.codangcoding.elmandroid.TEAComponent
import com.codangcoding.popularmovies.detail.DetailCmd.LoadReviewsCmd
import com.codangcoding.popularmovies.detail.DetailCmd.LoadVideosCmd
import com.codangcoding.popularmovies.detail.DetailMsg.*
import com.codangcoding.popularmovies.detail.DetailUIState.UpdateType.*
import com.codangcoding.popularmovies.internal.api.MovieDbService
import com.codangcoding.popularmovies.internal.api.model.Movie
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class DetailViewModel @Inject constructor(
        private val program: Program,
        private val movieDbService: MovieDbService
) : ViewModel(), TEAComponent {

    private val detailUIStateSubject = PublishSubject.create<DetailUIState>()
    private lateinit var programDisposable: Disposable

    fun init(movie: Movie) {
        programDisposable = program.init(DetailUIState(updateType = UPDATE_MOVIE, movie = movie), this)
        program.accept(ReviewsLoadMsg())
    }

    fun stateChanges(): Observable<DetailUIState> = detailUIStateSubject

    override fun onCleared() {
        super.onCleared()
        programDisposable.dispose()
    }

    override fun update(msg: Msg, state: State): Pair<State, Cmd> {
        val detailUIState = state as DetailUIState

        return when (msg) {
            is ReviewsLoadMsg ->
                detailUIState to LoadReviewsCmd(detailUIState.movie.id)
            is ReviewsLoadedMsg ->
                detailUIState.copy(updateType = UPDATE_REVIEW, reviews = msg.reviews) to LoadVideosCmd(detailUIState.movie.id)
            is VideosLoadedMsg ->
                detailUIState.copy(updateType = UPDATE_VIDEO, videos = msg.videos) to None
            else ->
                detailUIState to None
        }
    }

    override fun view(state: State) {
        detailUIStateSubject.onNext(state as DetailUIState)
    }

    override fun call(cmd: Cmd): Single<Msg> {
        return when (cmd) {
            is LoadReviewsCmd ->
                movieDbService.getMovieReviews(cmd.movieId).map { reviews -> ReviewsLoadedMsg(reviews) }
            is LoadVideosCmd ->
                movieDbService.getMovieVideos(cmd.movieId).map { videos -> VideosLoadedMsg(videos) }
            else ->
                Single.just(Idle)
        }
    }

}