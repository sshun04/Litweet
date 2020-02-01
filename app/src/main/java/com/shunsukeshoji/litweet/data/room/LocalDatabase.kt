package com.shunsukeshoji.litweet.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.shunsukeshoji.litweet.domain.model.Account

@Database (entities = [Account::class],version = 2)
abstract class LocalDatabase:RoomDatabase() {
    abstract fun roomDao():RoomDao
}