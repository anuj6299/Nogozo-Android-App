package com.startup.startup.di

import com.startup.startup.di.auth.AuthFragmentModule
import com.startup.startup.di.auth.AuthScope
import com.startup.startup.di.auth.AuthViewModelModule
import com.startup.startup.di.inventory.InventoryScope
import com.startup.startup.di.inventory.VendorInventoryFragmentModule
import com.startup.startup.di.inventory.VendorInventoryViewModelModule
import com.startup.startup.di.main.MainFragmentModule
import com.startup.startup.di.main.MainModule
import com.startup.startup.di.main.MainScope
import com.startup.startup.di.main.MainViewModelModule
import com.startup.startup.di.orders.OrderFragmentModule
import com.startup.startup.di.orders.OrderScope
import com.startup.startup.di.orders.OrderViewModelModule
import com.startup.startup.di.payment.PaymentFragmentModule
import com.startup.startup.di.payment.PaymentScope
import com.startup.startup.di.payment.PaymentViewModelModule
import com.startup.startup.di.profile.ProfileFragmentModule
import com.startup.startup.di.profile.ProfileScope
import com.startup.startup.di.profile.ProfileViewModelModule
import com.startup.startup.di.splash.SplashViewModelModule
import com.startup.startup.di.userdetails.UserDetailsFragmentModule
import com.startup.startup.di.userdetails.UserDetailsScope
import com.startup.startup.di.userdetails.UserDetailsViewModelModule
import com.startup.startup.ui.BaseActivity
import com.startup.startup.ui.auth.AuthActivity
import com.startup.startup.ui.inventory.VendorInventoryActivity
import com.startup.startup.ui.main.MainActivity
import com.startup.startup.ui.orders.OrdersActivity
import com.startup.startup.ui.payment.ConfirmActivity
import com.startup.startup.ui.profile.ProfileActivity
import com.startup.startup.ui.userdetails.UserDetailsActivity
import com.startup.startup.ui.splash.SplashActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilderModule {

    @ContributesAndroidInjector
    abstract fun contributeBaseActivity(): BaseActivity

    @ContributesAndroidInjector(
        modules = [
            SplashViewModelModule::class
        ]
    )
    abstract fun contributeSplashActivity(): SplashActivity

    @AuthScope
    @ContributesAndroidInjector(
        modules = [
            AuthViewModelModule::class,
            AuthFragmentModule::class
        ]
    )
    abstract fun contributeAuthActivity(): AuthActivity

    @UserDetailsScope
    @ContributesAndroidInjector(
        modules = [
            UserDetailsFragmentModule::class,
            UserDetailsViewModelModule::class
        ]
    )
    abstract fun contributeCustomerIntroActivity(): UserDetailsActivity

    @MainScope
    @ContributesAndroidInjector(
        modules = [
            MainFragmentModule::class,
            MainViewModelModule::class,
            MainModule::class
        ]
    )
    abstract fun contributeMainActivity(): MainActivity

    @PaymentScope
    @ContributesAndroidInjector(
        modules = [
            PaymentFragmentModule::class,
            PaymentViewModelModule::class
        ]
    )
    abstract fun contributeConfirmActivity(): ConfirmActivity

    @ProfileScope
    @ContributesAndroidInjector(
        modules = [
            ProfileFragmentModule::class,
            ProfileViewModelModule::class
        ]
    )
    abstract fun contributeProfileActivity(): ProfileActivity

    @OrderScope
    @ContributesAndroidInjector(
        modules = [
            OrderFragmentModule::class,
            OrderViewModelModule::class
        ]
    )
    abstract  fun contributeOrderActivity(): OrdersActivity

    @InventoryScope
    @ContributesAndroidInjector(
        modules = [
            VendorInventoryViewModelModule::class,
            VendorInventoryFragmentModule::class
        ]
    )
    abstract fun contributeVendorInventoryActivity(): VendorInventoryActivity
}