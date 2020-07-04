package com.startup.startup.ui.main

interface Communicator {
    fun onServiceSelected(serviceId: String, serviceName: String)

    fun onShopSelected(shopId: String, shopName: String, shopAddress: String?)

    fun setToolbarTitle(title: String){}
}