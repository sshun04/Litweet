package com.shunsukeshoji.litweet.data.impl

import com.shunsukeshoji.litweet.data.room.RoomDao
import com.shunsukeshoji.litweet.domain.model.Account
import io.reactivex.Observable
import io.reactivex.Single

class LocalCacheRepository(private val dao: RoomDao) {
    fun insertAccount(account: Account) = dao.insertAccount(account)
    fun getAccounts(): Observable<List<Account>> = dao.getAllAccounts()
    fun deleteAllAccounts(accounts: List<Account>): Single<Int> = dao.deleteAllAccounts(accounts)
}