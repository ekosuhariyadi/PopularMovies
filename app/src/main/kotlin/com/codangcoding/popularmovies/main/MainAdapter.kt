package com.codangcoding.popularmovies.main

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.codangcoding.popularmovies.GlideApp
import com.codangcoding.popularmovies.R
import com.codangcoding.popularmovies.internal.api.ApiConstants
import com.codangcoding.popularmovies.internal.api.model.Movie
import com.codangcoding.popularmovies.internal.base.BaseAdapter
import kotlinx.android.synthetic.main.item_movie_list.view.*

typealias MovieClickListener = (Movie, View) -> Unit

class MainAdapter(private val context: Context, private val clickListener: MovieClickListener)
    : BaseAdapter<MainAdapter.MainViewHolder, Movie>() {

    private var data: List<Movie> = emptyList()

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val rootView = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.item_movie_list, parent, false)

        return MainViewHolder(rootView).apply {
            itemView.setOnClickListener { view ->
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    clickListener.invoke(data[position], view)
                }
            }
        }
    }

    override fun setData(data: List<Movie>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = data.count()


    inner class MainViewHolder(rootView: View) : RecyclerView.ViewHolder(rootView) {

        private val moviePoster = itemView.moviePoster

        fun bind(movie: Movie) {
            GlideApp.with(context)
                    .load("${ApiConstants.POSTER_BASE_URL}${movie.posterPath}")
                    .placeholder(R.drawable.placeholder)
                    .error(R.mipmap.ic_launcher_round)
                    .into(moviePoster)
        }
    }
}