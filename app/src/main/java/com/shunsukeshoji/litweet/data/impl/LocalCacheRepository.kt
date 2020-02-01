package com.shunsukeshoji.litweet.data.impl

import androidx.lifecycle.LiveData
import androidx.room.Room
import com.shunsukeshoji.litweet.data.room.LocalDatabase
import com.shunsukeshoji.litweet.data.room.RoomDao
import com.shunsukeshoji.litweet.domain.model.Account
import io.reactivex.Flowable

class LocalCacheRepository(private val dao: RoomDao) {
    fun insertAccount(account: Account) = dao.insertAccount(account)
    fun getAccounts(): List<Account> = dao.getAllAccounts()
    fun deleteAllAccounts(accounts:List<Account>) = dao.deleteAllAccounts(accounts)
}