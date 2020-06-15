package com.startup.startup.di.auth

import androidx.lifecycle.ViewModel
import com.startup.startup.di.ViewModelKey
import com.startup.startup.ui.auth.customer.CustomerAuthFragmentViewModel
import com.startup.startup.ui.auth.vendor.VendorAuthFragmentViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class AuthViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(CustomerAuthFragmentViewModel::class)
    abstract fun bindCustomerAuthViewModel(viewModel: CustomerAuthFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(VendorAuthFragmentViewModel::class)
    abstract fun bindVendorAuthViewModel(viewModel: VendorAuthFragmentViewModel): ViewModel
}