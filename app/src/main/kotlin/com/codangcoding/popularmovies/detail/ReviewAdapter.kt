package com.codangcoding.popularmovies.detail

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.codangcoding.popularmovies.R
import com.codangcoding.popularmovies.internal.api.model.Review
import com.codangcoding.popularmovies.internal.base.BaseAdapter
import kotlinx.android.synthetic.main.item_review_list.view.*


class ReviewAdapter(private val context: Context) : BaseAdapter<ReviewAdapter.ReviewViewHolder, Review>() {

    private var reviews: List<Review> = emptyList()

    override fun getItemCount(): Int =
            reviews.count()

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        holder.bind(reviews[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val rootView = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.item_review_list, parent, false)

        return ReviewViewHolder(rootView)
    }

    override fun setData(data: List<Review>) {
        reviews = data
        notifyDataSetChanged()
    }

    inner class ReviewViewHolder(rootView: View) : RecyclerView.ViewHolder(rootView) {

        private val reviewContent = itemView.reviewContent
        private val reviewAuthor = itemView.reviewAuthor

        fun bind(review: Review) {
            reviewContent.text = review.content
            reviewAuthor.text = "\" ${review.author} \""
        }
    }
}