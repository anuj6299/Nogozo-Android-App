package com.startup.startup.di.orders

import com.startup.startup.ui.main.vendor.orders.current.VendorCurrentOrdersFragment
import com.startup.startup.ui.orders.customer.CustomerOrdersFragment
import com.startup.startup.ui.orders.customer.current.CustomerCurrentOrdersFragment
import com.startup.startup.ui.orders.customer.past.CustomerPastOrdersFragment
import com.startup.startup.ui.orders.vendor.past.VendorPastOrdersFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class OrderFragmentModule{

    @ContributesAndroidInjector
    abstract fun contributeVendorPastOrdersFragment(): VendorPastOrdersFragment

    @ContributesAndroidInjector
    abstract fun contributeVendorCurrentOrdersFragment(): VendorCurrentOrdersFragment

    @ContributesAndroidInjector
    abstract fun contributeCustomerPastOrdersFragment(): CustomerPastOrdersFragment

    @ContributesAndroidInjector
    abstract fun contributeCustomerCurrentOrdersFragment(): CustomerCurrentOrdersFragment

    @ContributesAndroidInjector
    abstract  fun contributeCustomerOrdersFragment(): CustomerOrdersFragment
}