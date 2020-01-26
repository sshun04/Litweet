package com.shunsukeshoji.litweet.data.impl

import com.shunsukeshoji.litweet.data.DropBoxService
import com.shunsukeshoji.litweet.domain.model.Account
import com.shunsukeshoji.litweet.domain.model.Tweet
import io.reactivex.Single

class RetrofitClientRepository(private val apiClient: DropBoxService) {
    fun getAccounts(url: String): Single<List<Account>> = apiClient.getAccounts(url)
    fun getTweets(url: String): Single<List<Tweet>> = apiClient.getTweets(url)
}