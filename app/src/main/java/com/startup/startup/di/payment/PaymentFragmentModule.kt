package com.startup.startup.di.payment

import com.startup.startup.ui.payment.customer.confirm.CustomerConfirmFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class PaymentFragmentModule{

    @ContributesAndroidInjector
    abstract fun contributeCustomerConfirmFragment(): CustomerConfirmFragment
}