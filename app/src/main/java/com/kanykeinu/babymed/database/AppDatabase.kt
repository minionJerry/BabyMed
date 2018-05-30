package com.kanykeinu.babymed.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.kanykeinu.babymed.dao.ChildDao
import com.kanykeinu.babymed.dao.IllnessDao
import com.kanykeinu.babymed.model.Child
import com.kanykeinu.babymed.model.Illness
import android.arch.persistence.room.Room
import android.content.Context


@Database(entities = arrayOf(Child::class,Illness::class), version = 4)
abstract class AppDatabase :RoomDatabase() {
    abstract fun childDao(): ChildDao
    abstract fun illnessDao(): IllnessDao

    companion object {
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase? {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                AppDatabase::class.java!!, "BabyMed.db")
                                .fallbackToDestructiveMigration()
                                .allowMainThreadQueries()
                                .build()
                    }
                }
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}