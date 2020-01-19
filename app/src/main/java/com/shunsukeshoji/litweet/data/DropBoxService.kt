package com.shunsukeshoji.litweet.data

import com.shunsukeshoji.litweet.domain.model.Account
import com.shunsukeshoji.litweet.domain.model.Tweet
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Url

interface DropBoxService {
    @GET
    fun getAccounts(@Url url: String):Single<List<Account>>

    @GET
    fun getTweets(@Url url:String):Single<List<Tweet>>
}