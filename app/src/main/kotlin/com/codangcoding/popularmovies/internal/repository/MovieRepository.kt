package com.codangcoding.popularmovies.internal.repository

import com.codangcoding.popularmovies.internal.api.MovieDbService
import com.codangcoding.popularmovies.internal.api.model.Movie
import com.codangcoding.popularmovies.internal.api.model.MovieType.POPULAR
import com.codangcoding.popularmovies.internal.api.model.MovieType.TOP_RATED
import com.codangcoding.popularmovies.internal.db.MovieDao
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepository @Inject constructor(
        private val movieDao: MovieDao,
        private val movieDbService: MovieDbService
) {

    fun loadPopularMovies(): Single<List<Movie>> =
            movieDao.getMoviesByType(POPULAR)
                    .flatMap {
                        if (it.isEmpty())
                            movieDbService.getPopularMovies()
                                    .map { it.map { it.apply { movieType = POPULAR } } }
                        else
                            Single.just(it)
                    }

    fun loadTopRatedMovies(): Single<List<Movie>> =
            movieDao.getMoviesByType(TOP_RATED)
                    .flatMap {
                        if (it.isEmpty())
                            movieDbService.getTopRatedMovies()
                                    .map { it.map { it.apply { movieType = TOP_RATED } } }
                        else
                            Single.just(it)
                    }

    fun saveMovies(movies: List<Movie>) {
        movieDao.insertMovies(movies)
    }
}