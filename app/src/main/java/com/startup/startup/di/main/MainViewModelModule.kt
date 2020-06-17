package com.startup.startup.di.main

import androidx.lifecycle.ViewModel
import com.startup.startup.di.ViewModelKey
import com.startup.startup.ui.main.customer.CustomerServiceFragmentViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class MainViewModelModule{

    @MainScope
    @Binds
    @IntoMap
    @ViewModelKey(CustomerServiceFragmentViewModel::class)
    abstract fun bindCustomerViewModel(viewmodel: CustomerServiceFragmentViewModel): ViewModel
}