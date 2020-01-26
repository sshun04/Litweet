package com.shunsukeshoji.litweet.presentation.custom_view

import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import coil.api.load
import coil.transform.RoundedCornersTransformation
import com.shunsukeshoji.litweet.domain.model.Account
import com.shunsukeshoji.litweet.domain.model.Tweet
import kotlinx.android.synthetic.main.item_tweet.view.*

class TweetCellView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleArray: Int = 0
) : ConstraintLayout(context, attrs, defStyleArray) {

    fun build(account: Account, tweet: Tweet) {
        userName.text = account.name
        userId.text = "@${account.id}"
        profile_image.load(account.icon)
        replyId.apply {
            replyId.text = tweet.replyId
            visibility = if (tweet.replyId.isBlank()) View.GONE else View.VISIBLE
        }
        tweetTime.text = tweet.time
        tweetText.text = tweet.tweet

        when (tweet.tweetType) {
            "text" -> {
                image.visibility = View.GONE
                videoView.visibility = View.GONE
            }
            "video" -> {
                image.visibility = View.GONE
                videoView.apply {
                    setVideoURI(Uri.parse(tweet.videoUrl))
                    setOnClickListener {
                        start()
                    }
                }
                videoView.visibility = View.VISIBLE
            }
            "image" -> {
                image.apply {
                    visibility = View.VISIBLE
                    load(tweet.imageUrl) {
                        transformations(
                            RoundedCornersTransformation(
                                topRight = 16f,
                                topLeft = 16f,
                                bottomLeft = 16f,
                                bottomRight = 16f
                            )
                        )
                    }
                }
                videoView.visibility = View.GONE
            }
        }
    }
}