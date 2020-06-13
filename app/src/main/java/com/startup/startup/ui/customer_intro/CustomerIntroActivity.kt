package com.startup.startup.ui.customer_intro

import android.os.Bundle
import com.startup.startup.R
import com.startup.startup.ui.BaseActivity

class CustomerIntroActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_intro)
        startFragment()
    }

    private fun startFragment(){
        supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment, SelectCityFragment()).commit()
    }

}