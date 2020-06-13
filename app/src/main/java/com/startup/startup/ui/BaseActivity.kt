package com.startup.startup.ui

import android.view.View
import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.constraintlayout.widget.ConstraintLayout
import com.startup.startup.R
import dagger.android.support.DaggerAppCompatActivity

abstract class BaseActivity: DaggerAppCompatActivity(){

    private lateinit var progressBar: ProgressBar

    override fun setContentView(layoutResID: Int) {

        val constraintLayout = layoutInflater.inflate(R.layout.activity_base,null) as ConstraintLayout
        val frameLayout = constraintLayout.findViewById(R.id.activity_container) as FrameLayout

        progressBar = constraintLayout.findViewById(R.id.base_progressbar)

        layoutInflater.inflate(layoutResID, frameLayout, true)

        super.setContentView(layoutResID)
    }

    fun showProgressBar(visibility: Boolean) {
        progressBar.visibility = if(visibility) View.VISIBLE else View.GONE
    }

}