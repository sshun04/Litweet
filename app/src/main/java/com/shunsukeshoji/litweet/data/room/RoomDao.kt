package com.shunsukeshoji.litweet.data.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.shunsukeshoji.litweet.domain.model.Account
import io.reactivex.Flowable

@Dao
interface RoomDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAccount(account: Account)

    @Query("SELECT * FROM accounts")
    fun getAllAccounts(): List<Account>

    @Delete
    fun deleteAllAccounts(accounts: List<Account>)
}