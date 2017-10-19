package com.codangcoding.popularmovies.internal.api.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey
import android.os.Parcel
import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonIgnore
import paperparcel.PaperParcel


@Entity(tableName = Review.TABLE_NAME,
        foreignKeys = arrayOf(
                ForeignKey(
                        entity = Movie::class,
                        parentColumns = arrayOf(Movie.COLUMN_ID),
                        childColumns = arrayOf(Review.COLUMN_MOVIE_ID)
                )
        )
)
@PaperParcel
data class Review(
        @PrimaryKey
        @ColumnInfo(index = true, name = COLUMN_ID)
        val id: String,

        @ColumnInfo(name = COLUMN_AUTHOR)
        val author: String = "",

        @ColumnInfo(name = COLUMN_CONTENT)
        var content: String = "",

        @ColumnInfo(name = COLUMN_URL)
        val url: String = "",

        @ColumnInfo(index = true, name = COLUMN_MOVIE_ID)
        @JsonIgnore
        val movieId: Long
) : Parcelable {

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        PaperParcelReview.writeToParcel(this, dest, flags)
    }

    companion object {
        @JvmField
        val CREATOR = PaperParcelReview.CREATOR

        const val TABLE_NAME = "reviews"
        const val COLUMN_ID = "id"
        const val COLUMN_AUTHOR = "author"
        const val COLUMN_CONTENT = "content"
        const val COLUMN_URL = "url"
        const val COLUMN_MOVIE_ID = "movie_id"
    }
}
