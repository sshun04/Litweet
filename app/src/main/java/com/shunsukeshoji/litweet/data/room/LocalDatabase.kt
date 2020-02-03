package com.shunsukeshoji.litweet.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.shunsukeshoji.litweet.domain.model.Account

@Database(entities = [Account::class], version = 3)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun roomDao(): RoomDao

    companion object {
        @Volatile
        private var INSTANCE: LocalDatabase? = null

        fun getInstance(context: Context): LocalDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context): LocalDatabase =
            Room.databaseBuilder(
                context.applicationContext,
                LocalDatabase::class.java,
                "Litweet.db"
            ).build()
    }
}