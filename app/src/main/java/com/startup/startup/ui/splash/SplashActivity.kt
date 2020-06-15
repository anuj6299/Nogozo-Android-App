package com.startup.startup.ui.splash

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.google.android.material.button.MaterialButton
import com.startup.startup.R
import com.startup.startup.SessionManager
import com.startup.startup.datamodels.User
import com.startup.startup.ui.BaseActivity
import com.startup.startup.ui.ViewModelFactory
import com.startup.startup.ui.auth.AuthActivity
import com.startup.startup.ui.auth.AuthResource
import com.startup.startup.ui.userdetails.UserDetailsActivity
import com.startup.startup.util.Constants.PROFILE_LEVEL_0
import com.startup.startup.util.Constants.PROFILE_LEVEL_1
import com.startup.startup.util.Constants.USER_TYPE
import com.startup.startup.util.Constants.userType_CUSTOMER
import com.startup.startup.util.Constants.userType_VENDOR
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator
import javax.inject.Inject

class SplashActivity : BaseActivity(), View.OnClickListener {

    @Inject
    lateinit var factory: ViewModelFactory

    @Inject
    lateinit var sessionManager: SessionManager

    private lateinit var viewModel: SplashActivityViewModel
    private lateinit var viewPager: ViewPager
    private lateinit var dots: WormDotsIndicator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        viewModel = ViewModelProvider(this, factory)[SplashActivityViewModel::class.java]
        viewPager = findViewById(R.id.splash_viewpager)
        dots = findViewById(R.id.splash_dotindicator)

        //setUpViewPager()

        findViewById<MaterialButton>(R.id.to_customer_login).setOnClickListener(this)
        findViewById<MaterialButton>(R.id.to_shop_login).setOnClickListener(this)

        subscribeObserver()

    }

//    private fun setUpViewPager(){
//        val viewPagerAdapter = ViewPagerAdapter(supportFragmentManager)
//        viewPager.adapter = viewPagerAdapter
//        dots.setViewPager(viewPager)
//    }

    private fun subscribeObserver(){
        viewModel.getCurrentUser().observe(this, Observer<AuthResource<User>>{
            when(it.Status){
                AuthResource.AuthStatus.AUTHENTICATED -> {
                    val userType = it.data.userType
                    if(userType == userType_CUSTOMER){
                        if(it.data.profileLevel == PROFILE_LEVEL_0){
                            val i = Intent(this@SplashActivity, UserDetailsActivity::class.java)
                            i.putExtra(USER_TYPE, userType_CUSTOMER)
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            startActivity(i)
                        }else if(it.data.profileLevel == PROFILE_LEVEL_1){
//                TODO(when userprofile is complete)
//                val i = Intent(this@SplashActivity, MainActivity::class.java)
//                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
//                startActivity(i)
                        }

                    }else if(userType == userType_VENDOR){
                        //val i = Intent(this@SplashActivity, Activity::class.java)
                        //startActivity(i)
                        //TODO PRIORITY MEDIUM
                    }
                }
                AuthResource.AuthStatus.LOADING -> {
                    showProgressBar(true)
                }
                AuthResource.AuthStatus.NOT_AUTHENTICATED -> {
                    showProgressBar(false)
                }
                AuthResource.AuthStatus.ERROR -> {
                    showProgressBar(false)
                }
            }
        })
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.to_customer_login -> {
                val i = Intent(this, AuthActivity::class.java)
                i.putExtra(USER_TYPE, userType_CUSTOMER)
                startActivity(i)
            }
            R.id.to_shop_login -> {
                Toast.makeText(this, "Coming Soon...", Toast.LENGTH_SHORT).show()
                return
//                val i: Intent = Intent(this, AuthActivity::class.java)
//                i.putExtra(USER_TYPE,"vendor")
//                startActivity(i)
            }
            else -> {
                Toast.makeText(this, "Nothing Click", Toast.LENGTH_SHORT).show()
            }
        }
    }
}