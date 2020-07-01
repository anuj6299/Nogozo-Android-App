package com.startup.startup.di.payment

import androidx.lifecycle.ViewModel
import com.startup.startup.di.ViewModelKey
import com.startup.startup.ui.payment.customer.confirm.CustomerConfirmFragmentViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class PaymentViewModelModule {

    @PaymentScope
    @Binds
    @IntoMap
    @ViewModelKey(CustomerConfirmFragmentViewModel::class)
    abstract fun bindCustomerServiceViewModel(viewModel: CustomerConfirmFragmentViewModel): ViewModel
}