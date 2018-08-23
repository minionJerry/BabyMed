package com.kanykeinu.babymed.di.modules

import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kanykeinu.babymed.view.addeditchild.NewChildActivity
import com.kanykeinu.babymed.view.addeditillness.NewIllnessActivity
import com.kanykeinu.babymed.view.childdetail.ChildDetailActivity
import com.kanykeinu.babymed.view.childrenlist.BottomNavigationDialogFragment
import com.kanykeinu.babymed.view.childrenlist.ChildrenActivity
import com.kanykeinu.babymed.view.illnessdetail.IllnessDetailActivity
import com.kanykeinu.babymed.view.singup.RegisterUserActivity
import com.kanykeinu.babymed.view.singup.SignInActivity
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

    @ContributesAndroidInjector
    abstract fun contributeIllnessDetailActivity() : IllnessDetailActivity

    @ContributesAndroidInjector
    abstract fun contributeRegisterUserActivity() : RegisterUserActivity

    @ContributesAndroidInjector
    abstract fun contributeSignInUserActivity() : SignInActivity







}
