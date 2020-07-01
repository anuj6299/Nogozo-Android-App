package com.startup.startup.di.main

import androidx.lifecycle.ViewModel
import com.startup.startup.di.ViewModelKey
import com.startup.startup.ui.main.customer.itemsInShop.ItemsInShopFragmentViewModel
import com.startup.startup.ui.main.customer.shops.ShopListFragmentViewModel
import com.startup.startup.ui.main.customer.services.CustomerServiceFragmentViewModel
import com.startup.startup.ui.main.vendor.orders.current.VendorCurrentOrdersFragmentViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class MainViewModelModule{

    @MainScope
    @Binds
    @IntoMap
    @ViewModelKey(CustomerServiceFragmentViewModel::class)
    abstract fun bindCustomerServiceViewModel(viewModel: CustomerServiceFragmentViewModel): ViewModel

    @MainScope
    @Binds
    @IntoMap
    @ViewModelKey(ShopListFragmentViewModel::class)
    abstract fun bindShopsViewModel(viewModel: ShopListFragmentViewModel): ViewModel

    @MainScope
    @Binds
    @IntoMap
    @ViewModelKey(ItemsInShopFragmentViewModel::class)
    abstract fun bindItemInShopViewModel(viewModel: ItemsInShopFragmentViewModel): ViewModel

    @MainScope
    @Binds
    @IntoMap
    @ViewModelKey(VendorCurrentOrdersFragmentViewModel::class)
    abstract fun bindCurrentOrderVendorViewModel(viewModel: VendorCurrentOrdersFragmentViewModel): ViewModel
}