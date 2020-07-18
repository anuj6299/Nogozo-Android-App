package com.startup.startup.di.stats

import androidx.lifecycle.ViewModel
import com.startup.startup.di.ViewModelKey
import com.startup.startup.ui.stats.vendor.VendorStatsFragmentViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class StatsFragmentViewModelModule {

    @StatsScope
    @Binds
    @IntoMap
    @ViewModelKey(VendorStatsFragmentViewModel::class)
    abstract fun bindVendorProfileViewModel(viewModel: VendorStatsFragmentViewModel): ViewModel
}