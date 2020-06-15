package com.startup.startup.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.startup.startup.R
import com.startup.startup.ui.BaseActivity
import com.startup.startup.ui.auth.customer.CustomerAuthFragment
import com.startup.startup.ui.auth.vendor.VendorAuthFragment
import com.startup.startup.util.Constants.USER_TYPE
import com.startup.startup.util.Constants.userType_CUSTOMER
import com.startup.startup.util.Constants.userType_VENDOR

class AuthActivity : BaseActivity() {

    private var userType: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        userType = intent.getStringExtra(USER_TYPE)

        if(userType.equals(userType_CUSTOMER))
            startFragment(CustomerAuthFragment())
        else if(userType.equals(userType_VENDOR))
            startFragment(VendorAuthFragment())

    }

    private fun startFragment(fragment: Fragment){
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.auth_container, fragment)
        ft.commit()
    }
}