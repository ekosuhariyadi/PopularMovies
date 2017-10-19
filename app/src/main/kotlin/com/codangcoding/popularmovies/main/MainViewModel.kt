package com.codangcoding.popularmovies.main

import android.arch.lifecycle.ViewModel
import com.codangcoding.elmandroid.AbstractCmd.Cmd
import com.codangcoding.elmandroid.AbstractCmd.None
import com.codangcoding.elmandroid.AbstractMsg.*
import com.codangcoding.elmandroid.Program
import com.codangcoding.elmandroid.State
import com.codangcoding.elmandroid.TEAComponent
import com.codangcoding.popularmovies.internal.repository.MovieRepository
import com.codangcoding.popularmovies.main.MainCmd.*
import com.codangcoding.popularmovies.main.MainMsg.MovieType.POPULAR
import com.codangcoding.popularmovies.main.MainMsg.MovieType.TOP_RATED
import com.codangcoding.popularmovies.main.MainMsg.MoviesLoadMsg
import com.codangcoding.popularmovies.main.MainMsg.MoviesLoadedMsg
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class MainViewModel @Inject constructor(
        private val program: Program,
        private val movieRepository: MovieRepository
) : ViewModel(), TEAComponent {

    private val mainUIStateSubject = PublishSubject.create<MainUIState>()
    private val programDisposable: Disposable = program.init(MainUIState(), this)

    fun stateChanges(): Observable<MainUIState> = mainUIStateSubject

    fun loadPopularMovies() {
        program.accept(MoviesLoadMsg(POPULAR))
    }

    fun loadTopRatedMovies() {
        program.accept(MoviesLoadMsg(TOP_RATED))
    }

    override fun onCleared() {
        super.onCleared()
        programDisposable.dispose()
    }

    override fun update(msg: Msg, state: State): Pair<State, Cmd> {
        val mainUIState = state as MainUIState

        fun getMovieLoadCmd(movieType: MainMsg.MovieType): Cmd =
                if (movieType == POPULAR)
                    LoadPopularMoviesCmd()
                else
                    LoadTopRatedMoviesCmd()

        return when (msg) {
            is MoviesLoadMsg ->
                mainUIState.copy(isLoading = true) to getMovieLoadCmd(msg.movieType)
            is MoviesLoadedMsg ->
                mainUIState.copy(isLoading = false, movies = msg.movies) to SaveMoviesCmd(msg.movies)
            is ErrorMsg ->
                mainUIState.copy(error = msg.error.message ?: "") to None
            else ->
                mainUIState to None
        }
    }

    override fun view(state: State) {
        mainUIStateSubject.onNext(state as MainUIState)
    }

    override fun call(cmd: Cmd): Single<Msg> =
            when (cmd) {
                is LoadPopularMoviesCmd ->
                    movieRepository.loadPopularMovies().map { movies -> MoviesLoadedMsg(movies) as Msg }
                is LoadTopRatedMoviesCmd ->
                    movieRepository.loadTopRatedMovies().map { movies -> MoviesLoadedMsg(movies) as Msg }
                is SaveMoviesCmd -> {
                    movieRepository.saveMovies(cmd.movies)
                    Single.just(Idle)
                }
                else ->
                    Single.just(Idle)
            }
}