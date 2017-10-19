package com.codangcoding.popularmovies.main

import com.codangcoding.elmandroid.AbstractCmd.Cmd
import com.codangcoding.elmandroid.AbstractMsg.Msg
import com.codangcoding.elmandroid.State
import com.codangcoding.popularmovies.internal.api.model.Movie

data class MainUIState(
        val isLoading: Boolean = false,
        val movies: List<Movie> = emptyList(),
        val error: String = ""
) : State()


sealed class MainMsg : Msg() {
    enum class MovieType { POPULAR, TOP_RATED }

    class MoviesLoadMsg(val movieType: MovieType) : MainMsg()
    class MoviesLoadedMsg(val movies: List<Movie>) : MainMsg()
}


sealed class MainCmd : Cmd() {
    class LoadPopularMoviesCmd : MainCmd()
    class LoadTopRatedMoviesCmd : MainCmd()
    class SaveMoviesCmd(val movies: List<Movie>) : MainCmd()
}