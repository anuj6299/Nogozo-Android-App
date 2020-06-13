package com.startup.startup.di.customer_intro

import com.startup.startup.ui.customer_intro.SelectCityFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class CustomerIntroFragmentModule {

    @ContributesAndroidInjector
    abstract fun contributeSelectCityFragment(): SelectCityFragment
}