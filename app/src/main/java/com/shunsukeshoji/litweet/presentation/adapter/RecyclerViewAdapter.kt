package com.shunsukeshoji.litweet.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.shunsukeshoji.litweet.R
import com.shunsukeshoji.litweet.domain.model.Account
import com.shunsukeshoji.litweet.domain.model.Tweet
import com.shunsukeshoji.litweet.presentation.custom_view.TweetCellView

class RecyclerViewAdapter(
    private val accounts: List<Account>
) : ListAdapter<Tweet, RecyclerViewAdapter.TweetViewHolder>(diffCallback) {

    companion object {
        private val diffCallback: DiffUtil.ItemCallback<Tweet> =
            object : DiffUtil.ItemCallback<Tweet>() {
                override fun areItemsTheSame(oldItem: Tweet, newItem: Tweet) =
                    oldItem == newItem

                override fun areContentsTheSame(oldItem: Tweet, newItem: Tweet) =
                    oldItem.number == newItem.number
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TweetViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_tweet, parent, false) as TweetCellView
        return TweetViewHolder(view)
    }

    override fun onBindViewHolder(holder: TweetViewHolder, position: Int) {
        getItem(position)?.let { tweet ->
            val account = accounts.find {
                it.id == tweet.id
            } ?: return
            holder.view.build(account, tweet)
        }
    }

    class TweetViewHolder(val view: TweetCellView) : RecyclerView.ViewHolder(view)
}