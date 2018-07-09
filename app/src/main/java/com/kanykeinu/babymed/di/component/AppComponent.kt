package com.kanykeinu.babymed.di.component

import com.kanykeinu.babymed.BabyMedApplication
import com.kanykeinu.babymed.di.modules.AppModule
import com.kanykeinu.babymed.di.modules.BuildersModule
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AndroidInjectionModule::class, BuildersModule::class, AppModule::class))
interface AppComponent {
    fun inject(app: BabyMedApplication)
}