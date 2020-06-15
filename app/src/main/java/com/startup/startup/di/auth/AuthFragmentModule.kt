package com.startup.startup.di.auth

import com.startup.startup.ui.auth.customer.CustomerAuthFragment
import com.startup.startup.ui.auth.vendor.VendorAuthFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class AuthFragmentModule {
    @ContributesAndroidInjector
    abstract fun contributeCustomerAuthFragment(): CustomerAuthFragment

    @ContributesAndroidInjector
    abstract fun contributeVendorAuthFragment(): VendorAuthFragment
}