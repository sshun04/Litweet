package com.shunsukeshoji.litweet.domain.model

import java.util.*

data class Tweet(
    val number: Int,
    val id: String,
    val replyId: String,
    val tweet: String,
    val tweetType: String,
    val imageUrl: String,
    val videoUrl: String,
    val time: String,
    override val dataId: String = UUID.randomUUID().toString()
) : DataHolder