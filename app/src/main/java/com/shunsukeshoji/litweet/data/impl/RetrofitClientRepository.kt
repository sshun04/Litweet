package com.shunsukeshoji.litweet.data.impl

import com.shunsukeshoji.litweet.data.api.DropBoxService
import com.shunsukeshoji.litweet.domain.model.Account
import com.shunsukeshoji.litweet.domain.model.Tweet
import io.reactivex.Observable
import io.reactivex.Single

class RetrofitClientRepository(private val dropBoxService: DropBoxService) {
    fun getAccounts(url: String): Single<List<Account>> = dropBoxService.getAccounts(url)
    fun getTweets(url: String): Single<List<Tweet>> = dropBoxService.getTweets(url)
}