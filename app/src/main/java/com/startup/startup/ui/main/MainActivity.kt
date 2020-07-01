package com.startup.startup.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
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
import com.startup.startup.ui.main.customer.itemsInShop.ItemsInShopFragment
import com.startup.startup.ui.main.customer.services.CustomerServicesFragment
import com.startup.startup.ui.main.customer.shops.ShopListFragment
import com.startup.startup.ui.main.vendor.orders.current.VendorCurrentOrdersFragment
import com.startup.startup.ui.orders.OrdersActivity
import com.startup.startup.ui.profile.ProfileActivity
import com.startup.startup.ui.splash.SplashActivity
import com.startup.startup.util.Constants.CURRENT_ORDER
import com.startup.startup.util.Constants.ORDER_TYPE
import com.startup.startup.util.Constants.PAST_ORDER
import com.startup.startup.util.Constants.SERVICE_ID
import com.startup.startup.util.Constants.SHOP_ID
import com.startup.startup.util.Constants.SHOP_NAME
import com.startup.startup.util.Constants.USER_TYPE
import com.startup.startup.util.Constants.userType_CUSTOMER
import com.startup.startup.util.Constants.userType_VENDOR
import javax.inject.Inject

class MainActivity : BaseActivity(), Communicator {

    @Inject
    lateinit var sessionManager: SessionManager

    private lateinit var shopDrawer: Drawer
    private lateinit var customerDrawer: Drawer
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar = findViewById(R.id.main_toolbar)
        setSupportActionBar(toolbar)

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
        customerDrawer = DrawerBuilder()
            .withActivity(this)
            .withToolbar(toolbar)
            .withTranslucentNavigationBar(true)
            .addDrawerItems(
                PrimaryDrawerItem().withIdentifier(0).withName("Current Orders").withSelectable(false).withTextColor(ContextCompat.getColor(this, R.color.colorPrimaryLight)),
                PrimaryDrawerItem().withIdentifier(1).withName("Past Orders").withSelectable(false).withTextColor(ContextCompat.getColor(this, R.color.colorPrimaryLight)),
                PrimaryDrawerItem().withIdentifier(2).withName("Profile").withSelectable(false).withTextColor(ContextCompat.getColor(this, R.color.colorPrimaryLight)),
                DividerDrawerItem().withIdentifier(3),
                SecondaryDrawerItem().withIdentifier(4).withName("Sign Out").withSelectable(false).withTextColor(ContextCompat.getColor(this, R.color.red))
            )
            .withOnDrawerItemClickListener(object: Drawer.OnDrawerItemClickListener{
                override fun onItemClick(view: View?, position: Int, drawerItem: IDrawerItem<*>): Boolean {
                    when(position){
                        0 -> {
                            val i = Intent(this@MainActivity, OrdersActivity::class.java)
                            i.putExtra(USER_TYPE, userType_CUSTOMER)
                            i.putExtra(ORDER_TYPE, CURRENT_ORDER)
                            startActivity(i)
                        }
                        1 -> {
                            val i = Intent(this@MainActivity, OrdersActivity::class.java)
                            i.putExtra(USER_TYPE, userType_CUSTOMER)
                            i.putExtra(ORDER_TYPE, PAST_ORDER)
                            startActivity(i)
                        }
                        2 -> {
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
                    customerDrawer.closeDrawer()
                    return true
                }
            })
            .build()
        customerDrawer.setSelection(-1, false)
    }

    private fun buildVendorDrawer(){
        shopDrawer = DrawerBuilder()
            .withActivity(this)
            .withToolbar(toolbar)
            .withTranslucentStatusBar(true)
            .addDrawerItems(
                PrimaryDrawerItem().withIdentifier(0).withName("Profile").withSelectable(false).withTextColor(ContextCompat.getColor(this, R.color.colorPrimaryLight)),
                SecondaryDrawerItem().withIdentifier(1).withName("Sign Out").withSelectable(false).withTextColor(ContextCompat.getColor(this, R.color.red))
            )
            .withOnDrawerItemClickListener(object: Drawer.OnDrawerItemClickListener{
                override fun onItemClick(view: View?, position: Int, drawerItem: IDrawerItem<*>): Boolean {
                    when(position){
                        0 -> {
                            val i = Intent(this@MainActivity, ProfileActivity::class.java)
                            i.putExtra(USER_TYPE, userType_VENDOR)
                            startActivity(i)
                        }
                        1 -> {
                            sessionManager.logout()
                            val i = Intent(this@MainActivity, SplashActivity::class.java)
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            startActivity(i)
                        }
                    }
                    shopDrawer.closeDrawer()
                    return true
                }
            })
            .build()
        shopDrawer.setSelection(-1, false)
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if(sessionManager.getUserType() == userType_VENDOR){
            menuInflater.inflate(R.menu.vendor_main_menu, menu)
            return true
        }
        return super.onCreateOptionsMenu(menu)
        //else
            //menuInflater.inflate(R.menu.customer_main_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_vendor_edit -> {
                Toast.makeText(this, "Coming Soon...", Toast.LENGTH_SHORT).show()
            }
            R.id.menu_vendor_history -> {
                Toast.makeText(this, "Coming Soon...", Toast.LENGTH_SHORT).show()
            }
        }
        return true
    }

    override fun onServiceSelected(serviceId: String) {
        //start fragment to show shops list
        val f = ShopListFragment(this)
        val b = Bundle()
        b.putString(SERVICE_ID, serviceId)
        f.arguments = b
        startFragment(f)
    }

    override fun onShopSelected(shopId: String, shopName: String) {
        //start fragment to show item list with specefic shopid
        val f = ItemsInShopFragment(this)
        val b = Bundle()
        b.putString(SHOP_ID, shopId)
        b.putString(SHOP_NAME, shopName)
        f.arguments = b
        startFragment(f)
    }

    override fun setToolbarTitle(title: String) {
        toolbar.title = title
    }
}