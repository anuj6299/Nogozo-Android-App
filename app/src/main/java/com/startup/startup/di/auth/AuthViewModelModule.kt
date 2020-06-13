package com.startup.startup.di.auth

import androidx.lifecycle.ViewModel
import com.startup.startup.di.ViewModelKey
import com.startup.startup.ui.auth.AuthActivityViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class AuthViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(AuthActivityViewModel::class)
    abstract fun bindViewModel(authViewModel: AuthActivityViewModel): ViewModel
}