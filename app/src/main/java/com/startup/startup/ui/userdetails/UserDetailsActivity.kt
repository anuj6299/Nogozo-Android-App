package com.startup.startup.ui.userdetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.startup.startup.R
import com.startup.startup.SessionManager
import com.startup.startup.ui.BaseActivity
import com.startup.startup.ui.userdetails.customer.CustomerDetailsFragment
import com.startup.startup.ui.userdetails.vendor.VendorDetailsFragment
import com.startup.startup.util.Constants.USER_TYPE
import com.startup.startup.util.Constants.userType_CUSTOMER
import com.startup.startup.util.Constants.userType_VENDOR
import javax.inject.Inject

class UserDetailsActivity : BaseActivity() {

    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_details)

        val i = intent
        if(i.getStringExtra(USER_TYPE) == userType_CUSTOMER)
            startFragment(CustomerDetailsFragment())
        else if(i.getStringExtra(USER_TYPE) == userType_VENDOR)
            startFragment(VendorDetailsFragment())
    }

    private fun startFragment(fragment: Fragment){
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.userdetails_container, fragment)
        ft.commit()
    }
}