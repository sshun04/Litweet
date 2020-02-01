package com.shunsukeshoji.litweet.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "accounts")
data class Account(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "search_ids") val searchIds: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "icon") val icon: String,
    @ColumnInfo(name = "bio") val bio: String,
    @ColumnInfo(name = "tweet_url") val tweetUrl: String
)