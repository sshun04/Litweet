package com.shunsukeshoji.litweet.presentation.view

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shunsukeshoji.litweet.domain.model.Account
import com.shunsukeshoji.litweet.domain.model.Tweet
import com.shunsukeshoji.litweet.domain.use_case.MainActivityUseCase
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import org.koin.core.KoinComponent
import org.koin.core.inject

class MainActivityViewModel( private val useCase: MainActivityUseCase) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()
    private var isInitialized: Boolean = false
    val tweets: MutableLiveData<MutableList<Tweet>> = MutableLiveData()
    val accounts: MutableLiveData<List<Account>> = MutableLiveData()
    lateinit var searchIds: List<String>

    fun onStartActivity() {
        if (isInitialized) return
        loadAccounts()
    }

    fun onSubmitId(id: String, notFound: () -> Unit) {
        if (searchIds.contains(id)) {
            requestUser(id)
        } else {
            notFound()
        }
    }
    private fun loadAccounts() {
        val observable = useCase.getAccounts()
        observable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doAfterSuccess {
                if (it.isNotEmpty()) {
                    accounts.value = it
                }
                isInitialized = true
            }
            .doOnSubscribe { it.addTo(compositeDisposable) }
            .retry(3)
            .subscribe(object : DisposableSingleObserver<List<Account>>() {
                override fun onSuccess(t: List<Account>) {
                    searchIds = t.flatMap {
                        it.searchIds.split(",")
                    }
                    Log.d("Load Account", "searchIds $searchIds")
                }

                override fun onError(e: Throwable) {
                }
            })
    }


    private fun requestUser(id: String) {
        val user = accounts.value?.find { it.searchIds.contains(id) }
        val single = useCase.requestTweet(user?.tweetUrl ?: "")

        single.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : DisposableSingleObserver<List<Tweet>>() {
                override fun onSuccess(t: List<Tweet>) {
                    val sortedList = tweets.value?.apply {
                        addAll(t)
                    }?.sortedBy { it.number }

                    if (sortedList == null) tweets.postValue(t.toMutableList()) else tweets.postValue(
                        sortedList.toMutableList()
                    )
                }

                override fun onError(e: Throwable) {
                    Log.d("Error Load Tweet", e.message)
                }
            })

    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }

}