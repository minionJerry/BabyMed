package com.kanykeinu.babymed.data.source.local

import com.kanykeinu.babymed.data.source.local.entity.Child
import com.kanykeinu.babymed.data.source.local.entity.Illness
import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import com.kanykeinu.babymed.data.source.local.dao.ChildDao
import com.kanykeinu.babymed.data.source.local.dao.IllnessDao


@Database(entities = arrayOf(Child::class, Illness::class), version = 7)
abstract class BabyMedDatabase : RoomDatabase() {
    abstract fun childDao(): ChildDao
    abstract fun illnessDao(): IllnessDao
}