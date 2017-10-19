package com.codangcoding.popularmovies.internal.api.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey
import android.os.Parcel
import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import paperparcel.PaperParcel


@Entity(
        tableName = Video.TABLE_NAME,
        foreignKeys = arrayOf(
                ForeignKey(
                        entity = Movie::class,
                        parentColumns = arrayOf(Movie.COLUMN_ID),
                        childColumns = arrayOf(Video.COLUMN_MOVIE_ID)
                )
        )
)
@PaperParcel
data class Video(

        @PrimaryKey
        @ColumnInfo(index = true, name = COLUMN_ID)
        val id: String,

        @ColumnInfo(name = COLUMN_ISO_639_1)
        @JsonProperty("iso_639_1")
        val iso_639_1: String,

        @ColumnInfo(name = COLUMN_ISO_3166_1)
        @JsonProperty("iso_3166_1")
        val iso_3166_1: String,

        @ColumnInfo(name = COLUMN_KEY)
        val key: String,

        @ColumnInfo(name = COLUMN_NAME)
        val name: String,

        @ColumnInfo(name = COLUMN_SITE)
        val site: String,

        @ColumnInfo(name = COLUMN_SIZE)
        val size: Int,

        @ColumnInfo(name = COLUMN_TYPE)
        val type: String,

        @ColumnInfo(index = true, name = COLUMN_MOVIE_ID)
        @JsonIgnore
        val movieId: Long
) : Parcelable {

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        PaperParcelVideo.writeToParcel(this, dest, flags)
    }

    companion object {
        @JvmField
        val CREATOR = PaperParcelVideo.CREATOR

        const val TABLE_NAME = "videos"
        const val COLUMN_ID = "id"
        const val COLUMN_ISO_639_1 = "iso_639_1"
        const val COLUMN_ISO_3166_1 = "iso_3166_1"
        const val COLUMN_KEY = "key"
        const val COLUMN_NAME = "name"
        const val COLUMN_SITE = "site"
        const val COLUMN_SIZE = "size"
        const val COLUMN_TYPE = "type"
        const val COLUMN_MOVIE_ID = "movie_id"
    }
}