package com.startup.startup.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.startup.startup.R
import com.startup.startup.SessionManager
import com.startup.startup.ui.BaseActivity
import com.startup.startup.ui.ViewModelFactory
import com.startup.startup.ui.customer_intro.CustomerIntroActivity
import javax.inject.Inject

class AuthActivity : BaseActivity(), View.OnClickListener {

    @Inject
    lateinit var factory: ViewModelFactory

    @Inject
    lateinit var sessionManager: SessionManager

    private lateinit var viewModel: AuthActivityViewModel

    private var userType: String? = null

    private lateinit var emailField: TextInputEditText
    private lateinit var passwordField: TextInputEditText
    private lateinit var button: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        initViews()
        viewModel = ViewModelProvider(this, factory)[AuthActivityViewModel::class.java]

        userType = intent.getStringExtra("usertype")
    }

    private fun initViews(){
        emailField = findViewById(R.id.email_field)
        passwordField = findViewById(R.id.password_field)
        button = findViewById(R.id.login_button)
        button.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.login_button -> {
                val username = emailField.text.toString()
                val password = passwordField.text.toString()
                if(username.equals("1") && password.equals("password")){
                    if(userType!!.equals("customer")){
                        val i: Intent = Intent(this, CustomerIntroActivity::class.java)
                        startActivity(i)
                        finish()
                    }else if(userType!!.equals("shop")){
                        Toast.makeText(this, "Coming Soon...", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}