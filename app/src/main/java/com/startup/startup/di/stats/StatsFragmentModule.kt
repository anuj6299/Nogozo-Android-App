package com.startup.startup.di.stats

import com.startup.startup.ui.stats.vendor.VendorStatsFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class StatsFragmentModule {

    @ContributesAndroidInjector
    abstract fun contributeVendorStatsFragment(): VendorStatsFragment
}