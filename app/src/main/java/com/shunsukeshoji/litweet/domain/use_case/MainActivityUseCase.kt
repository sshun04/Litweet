package com.shunsukeshoji.litweet.domain.use_case

import android.util.Log
import com.shunsukeshoji.litweet.data.impl.LocalCacheRepository
import com.shunsukeshoji.litweet.data.impl.RetrofitClientRepository
import com.shunsukeshoji.litweet.domain.model.Account
import com.shunsukeshoji.litweet.domain.model.Tweet
import io.reactivex.Observable
import io.reactivex.Single

class MainActivityUseCase(private val retrofitClientRepository: RetrofitClientRepository,private val localCacheRepository: LocalCacheRepository) {
    companion object {
        const val ACCOUNT_URL = "s/kpmqjhb7qis5mri/accounts.json"
        const val BASE_URL = "https://dl.dropboxusercontent.com/"
    }

    fun loadAccounts(): Single<List<Account>> = retrofitClientRepository.getAccounts(ACCOUNT_URL)

    fun requestTweet(baseUrl: String): Single<List<Tweet>> {
        val url = baseUrl.substringAfter(BASE_URL)
        Log.d("Url", url)
        return retrofitClientRepository.getTweets(url)
    }

    fun getCachedAccount():Observable<List<Account>> = localCacheRepository.getAccounts()

    fun addSubmittedAccount(account: Account) = localCacheRepository.insertAccount(account)

    fun resetLocalCache(accounts:List<Account>) = localCacheRepository.deleteAllAccounts(accounts)
}