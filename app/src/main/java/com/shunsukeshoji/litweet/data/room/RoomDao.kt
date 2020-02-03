package com.shunsukeshoji.litweet.data.room

import androidx.room.*
import com.shunsukeshoji.litweet.domain.model.Account
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface RoomDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAccount(account: Account)

    @Query("SELECT * FROM accounts")
    fun getAllAccounts(): Observable<List<Account>>

    @Delete
    fun deleteAllAccounts(accounts: List<Account>):Single<Int>
}