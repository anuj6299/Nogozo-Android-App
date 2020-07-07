package com.startup.startup.di.profile

import com.startup.startup.ui.profile.customer.CustomerProfileFragment
import com.startup.startup.ui.profile.vendor.VendorProfileFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ProfileFragmentModule {
    @ContributesAndroidInjector
    abstract fun contributeCustomerProfileFragment(): CustomerProfileFragment

    @ContributesAndroidInjector
    abstract fun contributeVendorProfileFragment(): VendorProfileFragment
}