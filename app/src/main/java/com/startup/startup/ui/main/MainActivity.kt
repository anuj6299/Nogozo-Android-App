package com.startup.startup.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.startup.startup.R
import com.startup.startup.ui.BaseActivity
import com.startup.startup.ui.main.customer.CustomerServicesFragment
import com.startup.startup.util.Constants

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val i = intent
        if(i.getStringExtra(Constants.USER_TYPE) == Constants.userType_CUSTOMER)
            startFragment(CustomerServicesFragment())
        else if(i.getStringExtra(Constants.USER_TYPE) == Constants.userType_VENDOR)
            println("MAIN: VENDOR")//startFragment(VendorMainFragment())
    }

    private fun startFragment(fragment: Fragment){
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.main_container, fragment)
        ft.commit()
    }
}