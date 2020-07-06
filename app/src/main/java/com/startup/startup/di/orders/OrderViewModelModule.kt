package com.startup.startup.di.orders

import androidx.lifecycle.ViewModel
import com.startup.startup.di.ViewModelKey
import com.startup.startup.ui.orders.customer.current.CustomerCurrentOrdersFragmentViewModel
import com.startup.startup.ui.orders.customer.past.CustomerPastOrdersFragmentViewModel
import com.startup.startup.ui.orders.vendor.past.VendorPastOrdersFragmentViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class OrderViewModelModule {
    @OrderScope
    @Binds
    @IntoMap
    @ViewModelKey(CustomerCurrentOrdersFragmentViewModel::class)
    abstract fun bindCustomerCurrentOrdersViewModel(viewModel: CustomerCurrentOrdersFragmentViewModel): ViewModel

    @OrderScope
    @Binds
    @IntoMap
    @ViewModelKey(CustomerPastOrdersFragmentViewModel::class)
    abstract fun bindCustomerPastOrdersViewModel(viewModel: CustomerPastOrdersFragmentViewModel): ViewModel

    @OrderScope
    @Binds
    @IntoMap
    @ViewModelKey(VendorPastOrdersFragmentViewModel::class)
    abstract fun bindVendorPastOrdersViewModel(viewModel: VendorPastOrdersFragmentViewModel): ViewModel
}