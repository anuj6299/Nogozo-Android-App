package com.startup.startup.di

import com.startup.startup.di.auth.AuthModule
import com.startup.startup.di.auth.AuthViewModelModule
import com.startup.startup.di.customer_intro.CustomerIntroFragmentModule
import com.startup.startup.di.customer_intro.CustomerIntroModule
import com.startup.startup.di.splash.SplashViewModelModule
import com.startup.startup.ui.BaseActivity
import com.startup.startup.ui.auth.AuthActivity
import com.startup.startup.ui.customer_intro.CustomerIntroActivity
import com.startup.startup.ui.splash.SplashActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilderModule {
    @ContributesAndroidInjector(
        modules = [
            SplashViewModelModule::class
        ]
    )
    abstract fun contributeSplashActivity(): SplashActivity

    @ContributesAndroidInjector(
        modules = [
            AuthModule::class,
            AuthViewModelModule::class
        ]
    )
    abstract fun contributeAuthActivity(): AuthActivity

    @ContributesAndroidInjector
    abstract fun contributeBaseActivity(): BaseActivity

    @ContributesAndroidInjector(
        modules = [
            CustomerIntroFragmentModule::class,
            CustomerIntroModule::class
        ]
    )
    abstract fun contributeCustomerIntroActivity(): CustomerIntroActivity

}