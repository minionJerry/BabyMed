package com.kanykeinu.babymed.di.modules

import com.kanykeinu.babymed.view.addeditchild.NewChildActivity
import com.kanykeinu.babymed.view.addeditillness.NewIllnessActivity
import com.kanykeinu.babymed.view.childdetail.ChildDetailActivity
import com.kanykeinu.babymed.view.childrenlist.ChildrenActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class BuildersModule {

    @ContributesAndroidInjector
    abstract fun contributeChildrenActivity() : ChildrenActivity

    @ContributesAndroidInjector
    abstract fun contributeNewChildActivity() : NewChildActivity

    @ContributesAndroidInjector
    abstract fun contributeNewIllnessActivity() : NewIllnessActivity

    @ContributesAndroidInjector
    abstract fun contributeChildDetailActivity() : ChildDetailActivity


}