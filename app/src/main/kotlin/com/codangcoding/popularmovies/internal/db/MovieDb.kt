package com.codangcoding.popularmovies.internal.db

import android.arch.persistence.room.*
import com.codangcoding.popularmovies.internal.api.model.*
import io.reactivex.Single

@Database(
        entities = arrayOf(
                Movie::class,
                Review::class,
                Video::class
        ),
        version = 1,
        exportSchema = false
)
@TypeConverters(MovieTypeConverter::class)
abstract class MovieDb : RoomDatabase() {

    abstract fun movieDao(): MovieDao

    abstract fun reviewDao(): ReviewDao

    abstract fun videoDao(): VideoDao
}

@Dao
interface MovieDao {

    @Query("SELECT * FROM ${Movie.TABLE_NAME} WHERE ${Movie.COLUMN_MOVIE_TYPE} = :movieType")
    fun getMoviesByType(movieType: MovieType): Single<List<Movie>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovies(movies: List<Movie>)
}

@Dao
interface ReviewDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(review: Review)
}

@Dao
interface VideoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(video: Video)
}