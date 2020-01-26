package com.shunsukeshoji.litweet.presentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shunsukeshoji.litweet.domain.model.Account
import com.shunsukeshoji.litweet.domain.model.Tweet
import com.shunsukeshoji.litweet.domain.use_case.MainActivityUseCase
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import org.koin.core.KoinComponent
import org.koin.core.inject

class MainActivityViewModel : ViewModel(), KoinComponent {
    private val useCase: MainActivityUseCase by inject()

    private val compositeDisposable = CompositeDisposable()
    private var searchIds: List<String>? = null

    private val _tweets: MutableLiveData<List<Tweet>> = MutableLiveData()
    val tweets: LiveData<List<Tweet>> = _tweets

    private val submittedAccounts: MutableList<Account> = mutableListOf()

    private val _accounts: MutableLiveData<List<Account>> = MutableLiveData()
    val accounts: LiveData<List<Account>> = _accounts

    private val _errorLiveData: MutableLiveData<Throwable> = MutableLiveData()
    val errorLiveData: LiveData<Throwable> = _errorLiveData

    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData()
    val isLoading: LiveData<Boolean> = _isLoading

    fun onStartActivity() {
        if (!accounts.value.isNullOrEmpty()) return
        loadAccounts()
    }

    fun requestTweets(id: String, reject: (String) -> Unit) =
        when {
            id == "reset" -> {
                reset()
            }
            (searchIds?.contains(id) == false) -> {
                reject("このアカウントは存在しません")
            }
            (submittedAccounts.find { it.searchIds.contains(id) } != null) -> {
                reject("このアカウントは追加ずみです")
            }
            else -> {
                loadTweets(id)
            }
        }

    private fun reset() {
        _tweets.postValue(null)
    }

    private fun loadAccounts() {
        val observable = useCase.getAccounts()
        observable
            .subscribeOn(Schedulers.io())
            .doOnSubscribe { _isLoading.postValue(true) }
            .doFinally { _isLoading.postValue(false) }
            .observeOn(Schedulers.io())
            .subscribeBy(
                onSuccess = {
                    if (it.isNotEmpty()) {
                        _accounts.postValue(it)
                    }
                    searchIds = it.flatMap {
                        it.searchIds.split(",")
                    }
                },
                onError = {
                    _errorLiveData.postValue(it)
                }
            )
            .addTo(compositeDisposable)
    }

    private fun loadTweets(id: String) {
        val user = accounts.value?.find { it.searchIds.contains(id) }
        useCase.requestTweet(user?.tweetUrl ?: return)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .doOnSubscribe { _isLoading.postValue(true) }
            .doFinally { _isLoading.postValue(false) }
            .subscribeBy(
                onSuccess = {
                    val sortedList = tweets.value
                        ?.plus(it)
                        ?.sortedBy { tweet -> tweet.number }
                        ?: it
                    _tweets.postValue(sortedList)
                    submittedAccounts.add(user)
                },
                onError = {
                    _errorLiveData.postValue(it)
                }
            )
            .addTo(compositeDisposable)
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }

}