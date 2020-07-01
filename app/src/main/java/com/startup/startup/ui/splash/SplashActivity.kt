package com.startup.startup.ui.splash

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.google.android.material.button.MaterialButton
import com.google.firebase.FirebaseApp
import com.startup.startup.R
import com.startup.startup.SessionManager
import com.startup.startup.ui.BaseActivity
import com.startup.startup.ui.ViewModelFactory
import com.startup.startup.ui.auth.AuthActivity
import com.startup.startup.ui.auth.AuthResource
import com.startup.startup.ui.main.MainActivity
import com.startup.startup.ui.userdetails.UserDetailsActivity
import com.startup.startup.util.Constants.PROFILE_LEVEL_0
import com.startup.startup.util.Constants.PROFILE_LEVEL_1
import com.startup.startup.util.Constants.USER_TYPE
import com.startup.startup.util.Constants.userType_CUSTOMER
import com.startup.startup.util.Constants.userType_VENDOR
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
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

        FirebaseApp.initializeApp(this)

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

        CoroutineScope(IO).launch{
            val auth = viewModel.getCurrentUser()

            when(auth.status){
                AuthResource.AuthStatus.AUTHENTICATED -> {
                    val userType = viewModel.getUserType()
                    if(userType == userType_CUSTOMER){
                        val profileLevel  = viewModel.getProfileLevel()
                        if(profileLevel == PROFILE_LEVEL_0){
                            val i = Intent(this@SplashActivity, UserDetailsActivity::class.java)
                            i.putExtra(USER_TYPE, userType_CUSTOMER)
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            startActivity(i)
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                        }else if(profileLevel == PROFILE_LEVEL_1){
                            val i = Intent(this@SplashActivity, MainActivity::class.java)
                            i.putExtra(USER_TYPE, userType_CUSTOMER)
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            startActivity(i)
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                        }
                    }else if(userType == userType_VENDOR){
                        val profileLevel = viewModel.getProfileLevel()
                        if(profileLevel == PROFILE_LEVEL_0){
                            val i = Intent(this@SplashActivity, UserDetailsActivity::class.java)
                            i.putExtra(USER_TYPE, userType_VENDOR)
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            startActivity(i)
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                        }else if(profileLevel == PROFILE_LEVEL_1){
                            val i = Intent(this@SplashActivity, MainActivity::class.java)
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            i.putExtra(USER_TYPE, userType_VENDOR)
                            startActivity(i)
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                        }
                    }
                }
                AuthResource.AuthStatus.NOT_AUTHENTICATED -> {}
            }
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.to_customer_login -> {
                val i = Intent(this, AuthActivity::class.java)
                i.putExtra(USER_TYPE, userType_CUSTOMER)
                startActivity(i)
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            }
            R.id.to_shop_login -> {
                val i = Intent(this, AuthActivity::class.java)
                i.putExtra(USER_TYPE, userType_VENDOR)
                startActivity(i)
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            }
            else -> {
                Toast.makeText(this, "Nothing Click", Toast.LENGTH_SHORT).show()
            }
        }
    }
}