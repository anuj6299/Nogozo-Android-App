package com.startup.startup.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.startup.startup.R
import com.startup.startup.SessionManager
import com.startup.startup.ui.BaseActivity
import com.startup.startup.ui.main.customer.itemsInShop.ItemsInShopFragment
import com.startup.startup.ui.main.customer.services.CustomerServicesFragment
import com.startup.startup.ui.main.customer.shops.ShopListFragment
import com.startup.startup.util.Constants
import com.startup.startup.util.Constants.SERVICE_ID
import com.startup.startup.util.Constants.SHOP_ID
import javax.inject.Inject

class MainActivity : BaseActivity(), Communicator {

    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val i = intent
        if(i.getStringExtra(Constants.USER_TYPE) == Constants.userType_CUSTOMER)
            startFragment(CustomerServicesFragment(this))
        else if(i.getStringExtra(Constants.USER_TYPE) == Constants.userType_VENDOR)
            println("MAIN: VENDOR")//startFragment(VendorMainFragment())
    }

    private fun startFragment(fragment: Fragment){
        val ft = supportFragmentManager.beginTransaction()
        ft.add(R.id.main_container, fragment)
        ft.addToBackStack(fragment.tag)
        ft.commit()
    }

    override fun onBackPressed() {
        if(supportFragmentManager.backStackEntryCount == 1)
            finish()
        else
            super.onBackPressed()
    }

    override fun onServiceSelected(serviceId: String) {
        //start fragment to show shops list
        val f = ShopListFragment(this)
        val b = Bundle()
        b.putString(SERVICE_ID, serviceId)
        f.arguments = b
        startFragment(f)
    }

    override fun onShopSelected(shopId: String) {
        val f = ItemsInShopFragment()
        val b = Bundle()
        b.putString(SHOP_ID, shopId)
        f.arguments = b
        startFragment(f)
    }
}