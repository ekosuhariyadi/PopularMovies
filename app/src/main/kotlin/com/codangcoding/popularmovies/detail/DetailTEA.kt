package com.codangcoding.popularmovies.detail

import com.codangcoding.elmandroid.AbstractCmd.Cmd
import com.codangcoding.elmandroid.AbstractMsg.Msg
import com.codangcoding.elmandroid.State
import com.codangcoding.popularmovies.internal.api.model.Movie
import com.codangcoding.popularmovies.internal.api.model.Review
import com.codangcoding.popularmovies.internal.api.model.Video

data class DetailUIState(
        val updateType: UpdateType,
        val movie: Movie,
        val reviews: List<Review> = emptyList(),
        val videos: List<Video> = emptyList()
) : State() {

    enum class UpdateType {
        UPDATE_MOVIE,
        UPDATE_REVIEW,
        UPDATE_VIDEO
    }
}


sealed class DetailMsg : Msg() {

    class VideosLoadedMsg(val videos: List<Video>) : DetailMsg()
    class ReviewsLoadMsg() : DetailMsg()
    class ReviewsLoadedMsg(val reviews: List<Review>) : DetailMsg()
}


sealed class DetailCmd : Cmd() {

    class LoadVideosCmd(val movieId: Long) : DetailCmd()
    class LoadReviewsCmd(val movieId: Long) : DetailCmd()
}