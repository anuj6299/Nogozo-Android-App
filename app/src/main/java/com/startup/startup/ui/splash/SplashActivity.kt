package com.startup.startup.ui.splash

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.button.MaterialButton
import com.startup.startup.R
import com.startup.startup.SessionManager
import com.startup.startup.datamodels.User
import com.startup.startup.ui.BaseActivity
import com.startup.startup.ui.ViewModelFactory
import com.startup.startup.ui.auth.AuthActivity
import com.startup.startup.util.AuthResource
import javax.inject.Inject

class SplashActivity : BaseActivity(), View.OnClickListener {

    @Inject
    lateinit var factory: ViewModelFactory

    @Inject
    lateinit var sessionManager: SessionManager

    private lateinit var viewModel: SplashActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        viewModel = ViewModelProvider(this, factory)[SplashActivityViewModel::class.java]

        findViewById<MaterialButton>(R.id.to_customer_login).setOnClickListener(this)
        findViewById<MaterialButton>(R.id.to_shop_login).setOnClickListener(this)

        subscriberObserver()

    }

    private fun subscriberObserver(){
        viewModel.getCurrentUser().observe(this, Observer<AuthResource<User>> {
            when(it.Status){
                AuthResource.AuthStatus.AUTHENTICATED -> {
                    //val i = Intent(this@SplashActivity, Activity::class.java)
                    //startActivity(i)
                    //TODO PRIORITY MEDIUM
                    finish()
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
                val i: Intent = Intent(this, AuthActivity::class.java)
                i.putExtra("usertype","customer")
                startActivity(i)
                finish()
            }
            R.id.to_shop_login -> {
                val i: Intent = Intent(this, AuthActivity::class.java)
                i.putExtra("usertype","shop")
                startActivity(i)
                finish()
            }
            else -> {
                Toast.makeText(this, "Nothing Click", Toast.LENGTH_SHORT).show()
            }
        }
    }
}