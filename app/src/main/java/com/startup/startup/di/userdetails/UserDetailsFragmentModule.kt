package com.startup.startup.di.userdetails

import com.startup.startup.ui.userdetails.customer.CustomerDetailsFragment
import com.startup.startup.ui.userdetails.vendor.VendorDetailsFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class UserDetailsFragmentModule {

    @ContributesAndroidInjector
    abstract fun contributeCustomerFragment(): CustomerDetailsFragment

    @ContributesAndroidInjector
    abstract fun contributeVendorFragment(): VendorDetailsFragment
}