package com.startup.startup.di.splash

import androidx.lifecycle.ViewModel
import com.startup.startup.di.ViewModelKey
import com.startup.startup.ui.splash.SplashActivityViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class SplashViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(SplashActivityViewModel::class)
    abstract fun bindViewModel(splashViewModel: SplashActivityViewModel): ViewModel
}