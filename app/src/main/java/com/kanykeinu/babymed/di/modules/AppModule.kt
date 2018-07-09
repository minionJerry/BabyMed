package com.kanykeinu.babymed.di.modules

import android.app.Application
import androidx.room.Room
import com.kanykeinu.babymed.data.source.local.BabyMedDatabase
import com.kanykeinu.babymed.data.source.local.dao.ChildDao
import com.kanykeinu.babymed.data.source.local.dao.IllnessDao
import com.kanykeinu.babymed.utils.Constants.DATABASE_NAME
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(val app : Application) {

    @Provides
    @Singleton
    fun provideApplication() : Application = app

    @Provides
    @Singleton
    fun provideDatabase() : BabyMedDatabase = Room.databaseBuilder(app,
            BabyMedDatabase::class.java, DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()


    @Provides
    @Singleton
    fun provideChildDao(
            database: BabyMedDatabase): ChildDao = database.childDao()

    @Provides
    @Singleton
    fun provideIllnessDao(
            database: BabyMedDatabase): IllnessDao = database.illnessDao()
}
