package com.startup.startup.di.main

import com.startup.startup.ui.main.customer.itemsInShop.ItemsInShopFragment
import com.startup.startup.ui.main.customer.search.GlobalSearchFragment
import com.startup.startup.ui.main.customer.shops.ShopListFragment
import com.startup.startup.ui.main.customer.services.CustomerServicesFragment
import com.startup.startup.ui.main.vendor.orders.current.VendorCurrentOrdersFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainFragmentModule {

    @ContributesAndroidInjector
    abstract fun contributeCustomerServicesFragment(): CustomerServicesFragment

    @ContributesAndroidInjector
    abstract fun contributeShopListFragment(): ShopListFragment

    @ContributesAndroidInjector
    abstract fun contributeItemsInShopFragment(): ItemsInShopFragment

    @ContributesAndroidInjector
    abstract fun contributeCurrentOrdersVendorFragment(): VendorCurrentOrdersFragment

    @ContributesAndroidInjector
    abstract  fun contributeGlobalSearchFragment(): GlobalSearchFragment
}