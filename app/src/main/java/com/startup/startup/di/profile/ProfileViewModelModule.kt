package com.startup.startup.di.profile

import androidx.lifecycle.ViewModel
import com.startup.startup.di.ViewModelKey
import com.startup.startup.ui.profile.customer.CustomerProfileFragmentViewModel
import com.startup.startup.ui.profile.vendor.VendorProfileFragmentViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ProfileViewModelModule {
    @ProfileScope
    @Binds
    @IntoMap
    @ViewModelKey(CustomerProfileFragmentViewModel::class)
    abstract fun bindCustomerProfileViewModel(viewModel: CustomerProfileFragmentViewModel): ViewModel

    @ProfileScope
    @Binds
    @IntoMap
    @ViewModelKey(VendorProfileFragmentViewModel::class)
    abstract fun bindVendorProfileViewModel(viewModel: VendorProfileFragmentViewModel): ViewModel
}