package com.startup.startup.ui.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.startup.startup.R
import com.startup.startup.SessionManager
import com.startup.startup.ui.BaseActivity
import com.startup.startup.ui.main.customer.itemsInShop.ItemsInShopFragment
import com.startup.startup.ui.main.customer.services.CustomerServicesFragment
import com.startup.startup.ui.main.customer.shops.ShopListFragment
import com.startup.startup.ui.main.vendor.orders.current.VendorCurrentOrdersFragment
import com.startup.startup.util.Constants.SERVICE_ID
import com.startup.startup.util.Constants.SERVICE_NAME
import com.startup.startup.util.Constants.SHOP_ADDRESS
import com.startup.startup.util.Constants.SHOP_ID
import com.startup.startup.util.Constants.SHOP_NAME
import com.startup.startup.util.Constants.USER_TYPE
import com.startup.startup.util.Constants.userType_CUSTOMER
import com.startup.startup.util.Constants.userType_VENDOR
import javax.inject.Inject

class MainActivity : BaseActivity(), Communicator, View.OnClickListener {

    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val userType = intent.getStringExtra(USER_TYPE)
        if(userType == userType_CUSTOMER)
            startFragment(CustomerServicesFragment(this))
        else if(userType == userType_VENDOR)
            startFragment(VendorCurrentOrdersFragment(this))
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

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        if(sessionManager.getUserType() == userType_VENDOR){
//            menuInflater.inflate(R.menu.vendor_main_menu, menu)
//            return true
//        }
//        return super.onCreateOptionsMenu(menu)
//        //else
//            //menuInflater.inflate(R.menu.customer_main_menu, menu)
//    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when(item.itemId){
//            R.id.menu_vendor_edit -> {
//                Toast.makeText(this, "Coming Soon...", Toast.LENGTH_SHORT).show()
//            }
//            R.id.menu_vendor_history -> {
//                Toast.makeText(this, "Coming Soon...", Toast.LENGTH_SHORT).show()
//            }
//        }
//        return true
//    }

    override fun onServiceSelected(serviceId: String, serviceName: String) {
        //start fragment to show shops list
        val f = ShopListFragment(this)
        val b = Bundle()
        b.putString(SERVICE_ID, serviceId)
        b.putString(SERVICE_NAME, serviceName)
        f.arguments = b
        startFragment(f)
    }

    override fun onShopSelected(shopId: String, shopName: String, shopAddress: String?) {
        //start fragment to show item list with specefic shopid
        val f = ItemsInShopFragment(this)
        val b = Bundle()
        b.putString(SHOP_ID, shopId)
        b.putString(SHOP_NAME, shopName)
        b.putString(SHOP_ADDRESS, shopAddress)
        f.arguments = b
        startFragment(f)
    }

    override fun onClick(v: View?) {
    }
}