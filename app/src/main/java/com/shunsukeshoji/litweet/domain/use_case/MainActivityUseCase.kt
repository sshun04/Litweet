package com.shunsukeshoji.litweet.domain.use_case

import android.util.Log
import com.shunsukeshoji.litweet.data.impl.LocalCacheRepository
import com.shunsukeshoji.litweet.data.impl.RetrofitClientRepository
import com.shunsukeshoji.litweet.domain.model.Account
import com.shunsukeshoji.litweet.domain.model.Tweet
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class MainActivityUseCase(
    private val retrofitClientRepository: RetrofitClientRepository,
    private val localCacheRepository: LocalCacheRepository
) {
    companion object {
        const val ACCOUNT_URL = "s/kpmqjhb7qis5mri/accounts.json"
        const val BASE_URL = "https://dl.dropboxusercontent.com/"
    }

    fun loadAccounts(): Single<List<Account>> =
        retrofitClientRepository
            .getAccounts(ACCOUNT_URL)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())

    fun requestTweet(baseUrl: String): Single<List<Tweet>> {
        val url = baseUrl.substringAfter(BASE_URL)
        Log.d("Url", url)
        return retrofitClientRepository.getTweets(url)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
    }

    fun getCachedAccount(): Observable<List<Account>> =
        localCacheRepository
            .getAccounts()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())

    fun addSubmittedAccount(account: Account) = localCacheRepository.insertAccount(account)

    fun resetLocalCache(accounts: List<Account>): Single<Int> =
        localCacheRepository
            .deleteAllAccounts(accounts)
            .subscribeOn(Schedulers.io())
}