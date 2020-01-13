package com.shunsukeshoji.litweet.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.shunsukeshoji.litweet.R
import com.shunsukeshoji.litweet.domain.model.Account
import com.shunsukeshoji.litweet.domain.model.Tweet
import com.shunsukeshoji.litweet.presentation.custom_view.TweetCellView

class RecyclerViewAdapter(
    private val accounts: List<Account>
) :
    RecyclerView.Adapter<RecyclerViewAdapter.TweetViewHolder>() {
    private var tweetList: List<Tweet>? = null

    fun setTweet(tweets: List<Tweet>) {
            this.tweetList = tweets
            notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TweetViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_tweet, parent, false) as TweetCellView
        return TweetViewHolder(view)
    }

    override fun onBindViewHolder(holder: TweetViewHolder, position: Int) {
        tweetList?.get(position)?.let { tweet ->
            val account = accounts.find {
                it.id == tweet.id
            } ?: return
            holder.view.build(account, tweet)
        }
    }

    override fun getItemCount(): Int = tweetList?.count() ?: 0

    class TweetViewHolder(val view: TweetCellView) : RecyclerView.ViewHolder(view)
}