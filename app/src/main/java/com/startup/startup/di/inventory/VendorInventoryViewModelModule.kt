package com.startup.startup.di.inventory

import androidx.lifecycle.ViewModel
import com.startup.startup.di.ViewModelKey
import com.startup.startup.ui.inventory.editinventory.EditInventoryFragmentViewModel
import com.startup.startup.ui.inventory.inventory.VendorInventoryFragmentViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class VendorInventoryViewModelModule {

    @InventoryScope
    @Binds
    @IntoMap
    @ViewModelKey(VendorInventoryFragmentViewModel::class)
    abstract fun bindVendorInventoryViewModel(viewModel: VendorInventoryFragmentViewModel): ViewModel

    @InventoryScope
    @Binds
    @IntoMap
    @ViewModelKey(EditInventoryFragmentViewModel::class)
    abstract fun bindVendorEditInventoryViewModel(viewModel: EditInventoryFragmentViewModel): ViewModel
}