package com.startup.startup.ui.profile

import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.startup.startup.R
import com.startup.startup.ui.BaseActivity
import com.startup.startup.ui.profile.customer.CustomerProfileFragment
import com.startup.startup.util.Constants.USER_TYPE
import com.startup.startup.util.Constants.userType_CUSTOMER
import com.startup.startup.util.Constants.userType_VENDOR

class ProfileActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val userType = intent.getStringExtra(USER_TYPE)
        if(userType == userType_CUSTOMER){
            startFragment(CustomerProfileFragment())
        }else if(userType == userType_VENDOR){
            Toast.makeText(this, "Coming Soon...", Toast.LENGTH_SHORT).show()
        }
    }

    private fun startFragment(fragment: Fragment){
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.profile_container, fragment)
        ft.addToBackStack(fragment.tag)
        ft.commit()
    }

    override fun onBackPressed() {
        if(supportFragmentManager.backStackEntryCount == 1)
            finish()
        else
            super.onBackPressed()
    }
}