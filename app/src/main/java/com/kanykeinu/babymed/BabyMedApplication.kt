package com.kanykeinu.babymed

import android.app.Activity
import android.app.Application
import com.kanykeinu.babymed.di.component.DaggerAppComponent
import com.kanykeinu.babymed.di.modules.AppModule
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

class BabyMedApplication : Application(), HasActivityInjector {

    @Inject
    lateinit var activityInjector: DispatchingAndroidInjector<Activity>

    override fun activityInjector(): AndroidInjector<Activity>  = activityInjector

    override fun onCreate() {
        super.onCreate()

        DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build().inject(this)
    }
}