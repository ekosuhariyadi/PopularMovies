package com.codangcoding.popularmovies.internal.di

import android.app.Application
import android.arch.persistence.room.Room
import com.codangcoding.popularmovies.internal.db.MovieDao
import com.codangcoding.popularmovies.internal.db.MovieDb
import com.codangcoding.popularmovies.internal.db.ReviewDao
import com.codangcoding.popularmovies.internal.db.VideoDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class PersistenceModule {

    @Provides
    @Singleton
    fun provideDb(application: Application): MovieDb =
            Room.databaseBuilder(application, MovieDb::class.java, "movie.db").build()

    @Provides
    @Singleton
    fun provideMovieDao(db: MovieDb): MovieDao =
            db.movieDao()

    @Provides
    @Singleton
    fun provideReviewDao(db: MovieDb): ReviewDao =
            db.reviewDao()

    @Provides
    @Singleton
    fun provideVideoDao(db: MovieDb): VideoDao =
            db.videoDao()
}