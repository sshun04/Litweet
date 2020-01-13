package com.shunsukeshoji.litweet.domain.model

data class Account(
    val id: String,
    val searchIds: String,
    val name: String,
    val icon: String,
    val bio: String,
    val tweetUrl: String
)