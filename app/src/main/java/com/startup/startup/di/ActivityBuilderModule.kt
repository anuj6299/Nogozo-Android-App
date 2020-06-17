package com.startup.startup.di

import com.startup.startup.di.auth.AuthFragmentModule
import com.startup.startup.di.auth.AuthModule
import com.startup.startup.di.auth.AuthScope
import com.startup.startup.di.auth.AuthViewModelModule
import com.startup.startup.di.main.MainFragmentModule
import com.startup.startup.di.main.MainScope
import com.startup.startup.di.main.MainViewModelModule
import com.startup.startup.di.splash.SplashViewModelModule
import com.startup.startup.di.userdetails.UserDetailsFragmentModule
import com.startup.startup.di.userdetails.UserDetailsModule
import com.startup.startup.di.userdetails.UserDetailsScope
import com.startup.startup.di.userdetails.UserDetailsViewModelModule
import com.startup.startup.ui.BaseActivity
import com.startup.startup.ui.auth.AuthActivity
import com.startup.startup.ui.main.MainActivity
import com.startup.startup.ui.userdetails.UserDetailsActivity
import com.startup.startup.ui.splash.SplashActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilderModule {

    @ContributesAndroidInjector
    abstract fun contributeBaseActivity(): BaseActivity

    @ContributesAndroidInjector(
        modules = [
            SplashViewModelModule::class
        ]
    )
    abstract fun contributeSplashActivity(): SplashActivity

    @AuthScope
    @ContributesAndroidInjector(
        modules = [
            AuthModule::class,
            AuthViewModelModule::class,
            AuthFragmentModule::class
        ]
    )
    abstract fun contributeAuthActivity(): AuthActivity

    @UserDetailsScope
    @ContributesAndroidInjector(
        modules = [
            UserDetailsFragmentModule::class,
            UserDetailsViewModelModule::class,
            UserDetailsModule::class
        ]
    )
    abstract fun contributeCustomerIntroActivity(): UserDetailsActivity

    @MainScope
    @ContributesAndroidInjector(
        modules = [
            MainFragmentModule::class,
            MainViewModelModule::class
        ]
    )
    abstract fun contributeMainActivity(): MainActivity
}