package com.shunsukeshoji.litweet

import android.util.Log
import com.shunsukeshoji.litweet.data.impl.RetrofitClientRepository
import com.shunsukeshoji.litweet.domain.model.Tweet
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import org.junit.Test
import org.koin.test.AutoCloseKoinTest
import org.koin.test.inject

class ApiRepositoryTest : AutoCloseKoinTest() {

    val apiClientRepository: RetrofitClientRepository by inject()

    @Test
    fun call_api() {

    }

    @Test
    fun get_accounts() {

    }

}