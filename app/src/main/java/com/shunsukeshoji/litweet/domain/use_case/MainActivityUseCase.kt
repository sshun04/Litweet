package com.shunsukeshoji.litweet.domain.use_case

import android.util.Log
import com.shunsukeshoji.litweet.data.repository.RetrofitClientRepository
import com.shunsukeshoji.litweet.domain.model.Account
import com.shunsukeshoji.litweet.domain.model.Tweet
import io.reactivex.Single

class MainActivityUseCase(private val retrofitClientRepository: RetrofitClientRepository) {
    companion object {
        const val ACCOUNT_URL = "s/kpmqjhb7qis5mri/accounts.json"
        const val BASE_URL = "https://dl.dropboxusercontent.com/"
    }

    fun getAccounts(): Single<List<Account>> = retrofitClientRepository.getAccounts(ACCOUNT_URL)

    fun requestTweet(baseUrl: String): Single<List<Tweet>> {
        val url = baseUrl.substringAfter(BASE_URL)
        Log.d("Url", url)
        return retrofitClientRepository.getTweets(url)
    }
}