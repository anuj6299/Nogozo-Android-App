package com.startup.startup.di.inventory

import com.startup.startup.ui.inventory.editinventory.EditInventoryFragment
import com.startup.startup.ui.inventory.inventory.VendorInventoryFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class VendorInventoryFragmentModule{
    @ContributesAndroidInjector
    abstract fun contributeVendorInventoryFragment(): VendorInventoryFragment

    @ContributesAndroidInjector
    abstract fun contributeEditInventoryFragment(): EditInventoryFragment
}