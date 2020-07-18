package com.startup.startup.ui.stats

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.startup.startup.R
import com.startup.startup.ui.BaseActivity
import com.startup.startup.ui.stats.vendor.VendorStatsFragment

class StatsActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stats)

        startFragment(VendorStatsFragment())
    }

    private fun startFragment(fragment: Fragment){
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.stats_container, fragment)
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