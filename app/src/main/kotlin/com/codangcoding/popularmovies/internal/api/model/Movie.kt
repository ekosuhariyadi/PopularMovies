package com.codangcoding.popularmovies.internal.api.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.TypeConverter
import android.os.Parcel
import android.os.Parcelable
import com.codangcoding.popularmovies.internal.api.model.MovieType.POPULAR
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import paperparcel.PaperParcel

enum class MovieType {
    POPULAR,
    TOP_RATED
}

class MovieTypeConverter {

    @TypeConverter
    fun fromInt(index: Int): MovieType = MovieType.values()[index]

    @TypeConverter
    fun toInt(movieType: MovieType): Int = movieType.ordinal
}

@Entity(tableName = Movie.TABLE_NAME)
@PaperParcel
data class Movie(

        @PrimaryKey
        @ColumnInfo(index = true, name = COLUMN_ID)
        @JsonProperty("id")
        val id: Long,

        @ColumnInfo(name = COLUMN_POSTER_PATH)
        @JsonProperty("poster_path")
        val posterPath: String,

        @ColumnInfo(name = COLUMN_BACKDROP_PATH)
        @JsonProperty("backdrop_path")
        val backdropPath: String,

        @ColumnInfo(name = COLUMN_ORIGINAL_TITLE)
        @JsonProperty("original_title")
        val originalTitle: String,

        @ColumnInfo(name = COLUMN_OVERVIEW)
        val overview: String,

        @ColumnInfo(name = COLUMN_TITLE)
        val title: String,

        @ColumnInfo(name = COLUMN_VOTE_AVERAGE)
        @JsonProperty("vote_average")
        val userRating: Double = 0.toDouble(),

        @ColumnInfo(name = COLUMN_RELEASE_DATE)
        @JsonProperty("release_date")
        val releaseDate: String,

        @ColumnInfo(name = COLUMN_FAVORITE)
        @JsonIgnore
        var favorite: Boolean = false,

        @ColumnInfo(name = COLUMN_MOVIE_TYPE)
        @JsonIgnore
        var movieType: MovieType = POPULAR
) : Parcelable {

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        PaperParcelMovie.writeToParcel(this, dest, flags)
    }

    companion object {
        @JvmField
        val CREATOR = PaperParcelMovie.CREATOR

        const val TABLE_NAME = "movies"
        const val COLUMN_ID = "id"
        const val COLUMN_POSTER_PATH = "poster_path"
        const val COLUMN_BACKDROP_PATH = "backdrop_path"
        const val COLUMN_ORIGINAL_TITLE = "original_title"
        const val COLUMN_OVERVIEW = "overview"
        const val COLUMN_TITLE = "title"
        const val COLUMN_VOTE_AVERAGE = "vote_average"
        const val COLUMN_RELEASE_DATE = "release_date"
        const val COLUMN_FAVORITE = "favorite"
        const val COLUMN_MOVIE_TYPE = "movie_type"
    }
}