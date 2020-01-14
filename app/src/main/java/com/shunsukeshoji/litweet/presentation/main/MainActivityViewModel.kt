package com.shunsukeshoji.litweet.presentation.main

import android.util.Log
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shunsukeshoji.litweet.domain.model.Account
import com.shunsukeshoji.litweet.domain.model.Tweet
import com.shunsukeshoji.litweet.domain.use_case.MainActivityUseCase
import com.shunsukeshoji.litweet.presentation.fragment.PostDialogFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers

class MainActivityViewModel(private val useCase: MainActivityUseCase) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()
    private var searchIds: List<String>? = null
    private var isInitialized: Boolean = false

    val tweets: LiveData<MutableList<Tweet>> get() = _tweets
    private val _tweets: MutableLiveData<MutableList<Tweet>> = MutableLiveData()

    val accounts: LiveData<List<Account>> get() = _accounts
    private val _accounts: MutableLiveData<List<Account>> = MutableLiveData()

    fun onStartActivity() {
        if (isInitialized) return
        loadAccounts()
    }

    fun setUpDialog(fragmentManager: FragmentManager) {
        if (isInitialized && !searchIds.isNullOrEmpty()) {
            val dialog = PostDialogFragment(
                searchIds ?: listOf()
            ) {
                requestUser(it)
            }
            dialog.show(fragmentManager,"")
        }
    }

    private fun loadAccounts() {
        val observable = useCase.getAccounts()
        observable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doAfterSuccess {
                if (it.isNotEmpty()) {
                    _accounts.value = it
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

                    if (sortedList == null) _tweets.postValue(t.toMutableList()) else _tweets.postValue(
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