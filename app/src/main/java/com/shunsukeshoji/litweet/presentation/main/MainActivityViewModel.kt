package com.shunsukeshoji.litweet.presentation.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shunsukeshoji.litweet.domain.model.Account
import com.shunsukeshoji.litweet.util.AccountValidationState
import com.shunsukeshoji.litweet.domain.model.Tweet
import com.shunsukeshoji.litweet.domain.use_case.MainActivityUseCase
import com.shunsukeshoji.litweet.util.ProcessErrorState
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.*
import io.reactivex.schedulers.Schedulers
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.lang.IllegalStateException
import java.net.UnknownHostException

class MainActivityViewModel : ViewModel(), KoinComponent {
    private val useCase: MainActivityUseCase by inject()

    private val compositeDisposable = CompositeDisposable()
    private var searchIds: List<String>? = null

    private val _tweets: MutableLiveData<List<Tweet>> = MutableLiveData()
    val tweets: LiveData<List<Tweet>> = _tweets

    private val submittedAccounts: MutableList<Account> = mutableListOf()

    private val _accounts: MutableLiveData<List<Account>> = MutableLiveData()
    val accounts: LiveData<List<Account>> = _accounts

    private val _errorLiveData: MutableLiveData<ProcessError> = MutableLiveData()
    val errorLiveData: LiveData<ProcessError> = _errorLiveData

    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData()
    val isLoading: LiveData<Boolean> = _isLoading

    fun initialize() {
        if (!accounts.value.isNullOrEmpty()) return
        loadMasterAccounts()
    }

    fun loadFromCache() {
        compositeDisposable.add(
            loadCachedAccounts()
                .map {
                    loadTweetsByCache(it)
                }
                .subscribeBy(
                    onNext = {
                        _tweets.postValue(it)
                    },
                    onError = {
                        _errorLiveData.postValue(ProcessError(it))
                    }
                )
        )
    }

    fun requestTweets(id: String, reject: (AccountValidationState) -> Unit) =
        when {
            id == "reset" -> {
                reset()
            }
            _accounts.value.isNullOrEmpty() -> {
                _errorLiveData.postValue(ProcessError(IllegalStateException()))
                reject(AccountValidationState.NOT_INITIALIZED)
            }
            (searchIds?.contains(id) == false) -> {
                reject(AccountValidationState.ACCOUNT_DOES_NOT_EXIST)
            }
            (submittedAccounts.find { it.searchIds.contains(id) } != null) -> {
                reject(AccountValidationState.ACCOUNT_ALREADY_SUBMITTED)
            }
            else -> {
                accounts.value?.find { it.searchIds.contains(id) }?.let {
                    loadTweets(account = it) {
                        submittedAccounts.add(it)
                        useCase.addSubmittedAccount(it)
                    }
                }
            }
        }

    private fun reset() {
        _tweets.postValue(null)
        submittedAccounts.clear()
        useCase.resetLocalCache(submittedAccounts)
    }

    private fun loadCachedAccounts(): Observable<List<Account>> = useCase.getCachedAccount()

    private fun loadMasterAccounts() {
        val observable = useCase.loadAccounts()
        observable
            .doOnSubscribe { _isLoading.postValue(true) }
            .doFinally { _isLoading.postValue(false) }
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
                    _errorLiveData.postValue(ProcessError(it))
                }
            )
            .addTo(compositeDisposable)
    }

    private fun loadTweets(account: Account, success: (() -> Unit) = {}) {
        useCase.requestTweet(account.tweetUrl)
            .doOnSubscribe { _isLoading.postValue(true) }
            .doFinally { _isLoading.postValue(false) }
            .subscribeBy(
                onSuccess = {
                    val sortedList = tweets.value
                        ?.plus(it)
                        ?.sortedBy { tweet -> tweet.number }
                        ?: it
                    _tweets.postValue(sortedList)
                    success()
                },
                onError = {
                    _errorLiveData.postValue(ProcessError(it))
                }
            )
            .addTo(compositeDisposable)
    }

    private fun loadTweetsByCache(accounts: List<Account>): List<Tweet> =
        Observable
            .fromIterable(accounts)
            .doOnError { _errorLiveData.postValue(ProcessError(it)) }
            .flatMap {
                submittedAccounts.add(it)
                useCase.requestTweet(it.tweetUrl).toObservable()
            }.reduce { t1: List<Tweet>, t2: List<Tweet> ->
                t1.plus(t2).sortedBy { tweet -> tweet.number }
            }.blockingGet()

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }

    class ProcessError(private val throwable: Throwable) {
        private var handledError = false

        val errorIfNotHandled: ProcessErrorState?
            get() {
                if (handledError) {
                    return null
                }
                handledError = true
                throwable.stackTrace
                return when (throwable) {
                    is UnknownHostException -> ProcessErrorState.BAD_CONNECTION
                    is IllegalStateException -> ProcessErrorState.NOT_INITIALIZED
                    else -> ProcessErrorState.UNKNOWN
                }
            }
    }
}