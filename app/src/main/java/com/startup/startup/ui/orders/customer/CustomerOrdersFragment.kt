package com.startup.startup.ui.orders.customer

import android.os.Bundle
import android.view.View
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.startup.startup.R
import com.startup.startup.ui.BaseFragment

class CustomerOrdersFragment: BaseFragment(R.layout.fragment_orders_customer){

    lateinit var viewPager: ViewPager
    lateinit var viewPagerAdapter: CustomerOrdersViewPagerAdapter
    lateinit var tabsLayout: TabLayout

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewPagerAdapter = CustomerOrdersViewPagerAdapter(childFragmentManager)
        viewPager = view.findViewById(R.id.viewpager)
        tabsLayout = view.findViewById(R.id.tabs)

        viewPager.adapter = viewPagerAdapter

        tabsLayout.setupWithViewPager(viewPager)
    }
}