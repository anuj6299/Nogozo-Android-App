package com.startup.startup.di.main

import com.startup.startup.ui.main.customer.CustomerServicesFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainFragmentModule {

    @ContributesAndroidInjector
    abstract fun contributeCustomerServicesFragment(): CustomerServicesFragment
}