package com.codangcoding.popularmovies.detail

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.NO_POSITION
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.codangcoding.popularmovies.GlideApp
import com.codangcoding.popularmovies.R
import com.codangcoding.popularmovies.internal.api.ApiConstants
import com.codangcoding.popularmovies.internal.api.model.Video
import com.codangcoding.popularmovies.internal.base.BaseAdapter
import kotlinx.android.synthetic.main.item_video_list.view.*

typealias VideoClickListener = (Video, View) -> Unit

class VideoAdapter(
        private val context: Context,
        private val listener: VideoClickListener
) : BaseAdapter<VideoAdapter.VideoViewHolder, Video>() {

    private var videos: List<Video> = emptyList()

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        holder.bind(videos[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val rootView = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.item_video_list, parent, false)

        return VideoViewHolder(rootView).apply {
            videoPlayBtn.setOnClickListener { view ->
                val position = adapterPosition
                if (position != NO_POSITION) {
                    listener.invoke(videos[position], view)
                }
            }
        }
    }

    override fun getItemCount(): Int = videos.count()

    override fun setData(data: List<Video>) {
        videos = data
        notifyDataSetChanged()
    }

    inner class VideoViewHolder(rootView: View) : RecyclerView.ViewHolder(rootView) {

        private val videoThumbnail: ImageView = itemView.videoThumbnail
        val videoPlayBtn: ImageView = itemView.videoPlayBtn

        fun bind(video: Video) {
            videoThumbnail.contentDescription = video.name
            GlideApp.with(context)
                    .load(String.format(ApiConstants.YOUTUBE_THUMBNAIL_URL, video.key))
                    .placeholder(R.drawable.placeholder)
                    .error(R.mipmap.ic_launcher_round)
                    .into(videoThumbnail)
        }
    }
}