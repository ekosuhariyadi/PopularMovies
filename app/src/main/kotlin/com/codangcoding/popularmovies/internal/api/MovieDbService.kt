package com.codangcoding.popularmovies.internal.api

import com.codangcoding.popularmovies.internal.api.model.Movie
import com.codangcoding.popularmovies.internal.api.model.Review
import com.codangcoding.popularmovies.internal.api.model.Video
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path


interface MovieDbService {

    @GET("movie/popular")
    fun getPopularMovies(): Single<List<Movie>>

    @GET("movie/top_rated")
    fun getTopRatedMovies(): Single<List<Movie>>

    @GET("movie/{movieId}/videos")
    fun getMovieVideos(@Path("movieId") movieId: Long): Single<List<Video>>

    @GET("movie/{movieId}/reviews")
    fun getMovieReviews(@Path("movieId") movieId: Long): Single<List<Review>>
}