package com.startup.startup.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.DividerDrawerItem
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import com.startup.startup.R
import com.startup.startup.SessionManager
import com.startup.startup.ui.BaseActivity
import com.startup.startup.ui.inventory.VendorInventoryActivity
import com.startup.startup.ui.main.customer.itemsInShop.ItemsInShopFragment
import com.startup.startup.ui.main.customer.search.GlobalSearchFragment
import com.startup.startup.ui.main.customer.services.CustomerServicesFragment
import com.startup.startup.ui.main.customer.shops.ShopListFragment
import com.startup.startup.ui.main.vendor.orders.current.VendorCurrentOrdersFragment
import com.startup.startup.ui.orders.OrdersActivity
import com.startup.startup.ui.profile.ProfileActivity
import com.startup.startup.ui.splash.SplashActivity
import com.startup.startup.ui.stats.StatsActivity
import com.startup.startup.util.Constants
import com.startup.startup.util.Constants.AREA_ID
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


    private lateinit var drawerButton: ImageButton
    private lateinit var drawer: Drawer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawerButton = findViewById(R.id.header_profile)
        drawerButton.setOnClickListener(this)

        val userType = intent.getStringExtra(USER_TYPE)
        if(userType == userType_CUSTOMER){
            buildCustomerDrawer()
            startFragment(CustomerServicesFragment(this))
        }
        else if(userType == userType_VENDOR){
            buildVendorDrawer()
            startFragment(VendorCurrentOrdersFragment(this))
        }
    }

    private fun buildCustomerDrawer(){
        //        account header
        drawer = DrawerBuilder()
            .withActivity(this)
            .withTranslucentNavigationBar(true)
            .withDrawerGravity(GravityCompat.END)
            .addDrawerItems(
                PrimaryDrawerItem().withIdentifier(0).withName("Orders").withSelectable(false).withTextColor(
                    ContextCompat.getColor(this, R.color.colorPrimaryLight)),
                PrimaryDrawerItem().withIdentifier(1).withName("Profile").withSelectable(false).withTextColor(
                    ContextCompat.getColor(this, R.color.colorPrimaryLight)),
                DividerDrawerItem().withIdentifier(2),
                SecondaryDrawerItem().withIdentifier(3).withName("Sign Out").withSelectable(false).withTextColor(
                    ContextCompat.getColor(this, R.color.red))
            )
            .withOnDrawerItemClickListener(object: Drawer.OnDrawerItemClickListener{
                override fun onItemClick(view: View?, position: Int, drawerItem: IDrawerItem<*>): Boolean {
                    when(position){
                        0 -> {
                            val i = Intent(this@MainActivity, OrdersActivity::class.java)
                            i.putExtra(USER_TYPE, userType_CUSTOMER)
                            startActivity(i)
                        }
                        98 -> {
                            val i = Intent(this@MainActivity, OrdersActivity::class.java)
                            i.putExtra(USER_TYPE, userType_CUSTOMER)
                            i.putExtra(Constants.ORDER_TYPE, Constants.CURRENT_ORDER)
                            startActivity(i)
                        }
                        99 -> {
                            val i = Intent(this@MainActivity, OrdersActivity::class.java)
                            i.putExtra(USER_TYPE, userType_CUSTOMER)
                            i.putExtra(Constants.ORDER_TYPE, Constants.PAST_ORDER)
                            startActivity(i)
                        }
                        1 -> {
                            val i = Intent(this@MainActivity, ProfileActivity::class.java)
                            i.putExtra(USER_TYPE, userType_CUSTOMER)
                            startActivity(i)
                        }
                        3 -> {
                            sessionManager.logout()
                            val i = Intent(this@MainActivity, SplashActivity::class.java)
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            startActivity(i)
                        }
                    }
                    drawer.closeDrawer()
                    return true
                }
            })
            .build()
        drawer.setSelection(-1, false)
    }

    private fun buildVendorDrawer(){
        drawer = DrawerBuilder()
            .withActivity(this)
            .withTranslucentStatusBar(true)
            .withDrawerGravity(GravityCompat.END)
            .addDrawerItems(
                PrimaryDrawerItem().withIdentifier(0).withName("Inventory").withSelectable(false).withTextColor(ContextCompat.getColor(this, R.color.colorPrimaryLight)),
                PrimaryDrawerItem().withIdentifier(1).withName("Past Orders").withSelectable(false).withTextColor(ContextCompat.getColor(this, R.color.colorPrimaryLight)),
                PrimaryDrawerItem().withIdentifier(2).withName("Stats").withSelectable(false).withTextColor(ContextCompat.getColor(this, R.color.colorPrimaryLight)),
                PrimaryDrawerItem().withIdentifier(3).withName("Profile").withSelectable(false).withTextColor(ContextCompat.getColor(this, R.color.colorPrimaryLight)),
                DividerDrawerItem(),
                SecondaryDrawerItem().withIdentifier(5).withName("Sign Out").withSelectable(false).withTextColor(ContextCompat.getColor(this, R.color.red))
            )
            .withOnDrawerItemClickListener(object: Drawer.OnDrawerItemClickListener{
                override fun onItemClick(view: View?, position: Int, drawerItem: IDrawerItem<*>): Boolean {
                    when(position){
                        0 -> {
                            val i = Intent(this@MainActivity, VendorInventoryActivity::class.java)
                            startActivity(i)
                        }
                        1-> {
                            val i = Intent(this@MainActivity, OrdersActivity::class.java)
                            i.putExtra(USER_TYPE, userType_VENDOR)
                            startActivity(i)
                        }
                        2-> {
                            val i = Intent(this@MainActivity, StatsActivity::class.java)
                            i.putExtra(USER_TYPE, userType_VENDOR)
                            startActivity(i)
                        }
                        3 -> {
                            val i = Intent(this@MainActivity, ProfileActivity::class.java)
                            i.putExtra(USER_TYPE, userType_VENDOR)
                            startActivity(i)
                        }
                        5 -> {
                            sessionManager.logout()
                            val i = Intent(this@MainActivity, SplashActivity::class.java)
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            startActivity(i)
                        }
                    }
                    drawer.closeDrawer()
                    return true
                }
            })
            .build()
        drawer.setSelection(-1, false)
    }

    private fun startFragment(fragment: Fragment){
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.main_container, fragment)
        ft.addToBackStack(fragment.tag)
        ft.commit()
    }

    override fun onBackPressed() {
        if(supportFragmentManager.backStackEntryCount == 1)
            finish()
        else
            super.onBackPressed()
    }

    override fun onServiceSelected(serviceId: String, serviceName: String) {
        //start fragment to show shops list
        val f = ShopListFragment(this)
        val b = Bundle()
        b.putString(SERVICE_ID, serviceId)
        b.putString(SERVICE_NAME, serviceName)
        f.arguments = b
        startFragment(f)
    }

    override fun onShopSelected(shopId: String, shopName: String, shopAddress: String?, shopAreaId: String) {
        //start fragment to show item list with specefic shopid
        val f = ItemsInShopFragment(this)
        val b = Bundle()
        b.putString(SHOP_ID, shopId)
        b.putString(SHOP_NAME, shopName)
        b.putString(SHOP_ADDRESS, shopAddress)
        b.putString(AREA_ID, shopAreaId)
        f.arguments = b
        startFragment(f)
    }

    override fun onGlobalSearch(){
        startFragment(GlobalSearchFragment(this))
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.header_profile -> {
                drawer.openDrawer()
            }
        }
    }
}