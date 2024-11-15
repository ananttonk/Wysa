package com.anant.wysa.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.anant.wysa.model.MovieListData

@Database(entities = [MovieListData.Result::class], version = 3)
abstract class WysaDataBase : RoomDatabase() {

    abstract fun movieDao(): MovieDAO

    companion object {
        @Volatile
        private var INSTANCES: WysaDataBase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = INSTANCES ?: synchronized(LOCK) {
            INSTANCES ?: createDataBase(context).also {
                INSTANCES = it
            }
        }

        private fun createDataBase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            WysaDataBase::class.java,
            "WysaDatabase"
        ).fallbackToDestructiveMigration().build()

    }
}