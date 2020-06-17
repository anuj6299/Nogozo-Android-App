package com.startup.startup.di.userdetails

import androidx.lifecycle.ViewModel
import com.startup.startup.di.ViewModelKey
import com.startup.startup.ui.userdetails.customer.CustomerDetailsFragmentViewModel
import com.startup.startup.ui.userdetails.vendor.VendorDetailsFragmentViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class UserDetailsViewModelModule {

    @UserDetailsScope
    @Binds
    @IntoMap
    @ViewModelKey(CustomerDetailsFragmentViewModel::class)
    abstract fun bindCustomerViewModel(viewmodel: CustomerDetailsFragmentViewModel): ViewModel

    @UserDetailsScope
    @Binds
    @IntoMap
    @ViewModelKey(VendorDetailsFragmentViewModel::class)
    abstract fun bindVendorViewModel(viewmodel: VendorDetailsFragmentViewModel): ViewModel
}