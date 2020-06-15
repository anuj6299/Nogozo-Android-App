package com.startup.startup.ui.auth.customer

import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.transition.TransitionManager
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.startup.startup.R
import com.startup.startup.ui.BaseFragment
import com.startup.startup.ui.ViewModelFactory
import com.startup.startup.ui.auth.AuthResource
import com.startup.startup.ui.userdetails.UserDetailsActivity
import com.startup.startup.util.Constants.PROFILE_LEVEL_0
import com.startup.startup.util.Constants.PROFILE_LEVEL_1
import com.startup.startup.util.Constants.USER_TYPE
import com.startup.startup.util.Constants.userType_CUSTOMER
import javax.inject.Inject

class CustomerAuthFragment: BaseFragment(R.layout.fragment_auth_customer_signin), View.OnClickListener {

    @Inject
    lateinit var factory: ViewModelFactory

    private lateinit var viewModel: CustomerAuthFragmentViewModel

    private val viewType_SIGNUP = 0
    private val viewType_SIGNIN = 1
    private var viewType = viewType_SIGNIN

    private lateinit var nameField: TextInputEditText
    private lateinit var emailField: TextInputEditText
    private lateinit var passwordField: TextInputEditText
    private lateinit var signinButton: MaterialButton
    private lateinit var signupButton: MaterialButton
    private lateinit var cc1: ConstraintLayout
    private lateinit var cc2: ConstraintLayout

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        nameField = view.findViewById(R.id.customer_name_field)
        emailField = view.findViewById(R.id.customer_email_field)
        passwordField = view.findViewById(R.id.customer_password_field)
        signinButton = view.findViewById(R.id.customer_login_button)
        signinButton.setBackgroundColor(resources.getColor(R.color.colorPrimary, resources.newTheme()))
        signinButton.setOnClickListener(this)
        signupButton = view.findViewById(R.id.customer_signup_button)
        signupButton.setOnClickListener(this)
        cc1 = view.findViewById(R.id.customer_auth_signin)

        viewModel = ViewModelProvider(this, factory)[CustomerAuthFragmentViewModel::class.java]

        viewModel.getCachedUser().removeObservers(viewLifecycleOwner)

        viewModel.getCachedUser().observe(viewLifecycleOwner, Observer{
            when(it.Status){
                AuthResource.AuthStatus.AUTHENTICATED -> {
                    //to next activity on user status
                    if(it.data.profileLevel == PROFILE_LEVEL_0){
                        val i =Intent(activity, UserDetailsActivity::class.java)
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        i.putExtra(USER_TYPE, userType_CUSTOMER)
                        startActivity(i)
                    }else if(it.data.profileLevel == PROFILE_LEVEL_1){
                        println(it.data.profileLevel)
                        //TODO PRIORITY MEDIUM implement activity for (authenticated && profileLevel > 0) user to land
                    }
                }
                AuthResource.AuthStatus.LOADING -> {
                    //show loading status
                    println("loading123")
                }
                AuthResource.AuthStatus.NOT_AUTHENTICATED -> {
                    //not authenticated
                    println("not authenticated 123")
                }
                AuthResource.AuthStatus.ERROR -> {
                    // error
                    println("error")
                }
            }
        })
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.customer_login_button -> {
                if(viewType == viewType_SIGNIN)
                    viewModel.authenticateUser(emailField.text.toString(), passwordField.text.toString(), userType_CUSTOMER)
                else if(viewType == viewType_SIGNUP)
                    animate()
            }
            R.id.customer_signup_button -> {
                if(viewType == viewType_SIGNUP)
                    viewModel.registerUser(nameField.text.toString(),emailField.text.toString(),passwordField.toString())
                else if(viewType == viewType_SIGNIN)
                    animate()
            }
        }
    }

    private fun animate(){

        viewType = if(viewType == viewType_SIGNIN) viewType_SIGNUP else viewType_SIGNIN

        val constraintSet1 = ConstraintSet()
        constraintSet1.clone(context, R.layout.fragment_auth_customer_signin)

        val constraintSet2 = ConstraintSet()
        constraintSet2.clone(context, R.layout.fragment_auth_customer_signup)

        TransitionManager.beginDelayedTransition(cc1)
        val constraint =
            if(viewType == viewType_SIGNIN){
                signupButton.text = "Don't Have Account? Register"
                signupButton.setTextColor(resources.getColor(R.color.colorPrimary, resources.newTheme()))
                signupButton.setBackgroundColor(resources.getColor(R.color.white, resources.newTheme()))
                signinButton.text = "Login"
                signinButton.setTextColor(resources.getColor(R.color.white, resources.newTheme()))
                signinButton.setBackgroundColor(resources.getColor(R.color.colorPrimary, resources.newTheme()))
                constraintSet1
            }else{
                signupButton.text = "Register"
                signupButton.setTextColor(resources.getColor(R.color.white, resources.newTheme()))
                signupButton.setBackgroundColor(resources.getColor(R.color.colorPrimary, resources.newTheme()))
                signinButton.text = "Already Have Account? Login"
                signinButton.setTextColor(resources.getColor(R.color.colorPrimary, resources.newTheme()))
                signinButton.setBackgroundColor(resources.getColor(R.color.white, resources.newTheme()))
                constraintSet2
            }
        constraint.applyTo(cc1)

    }
}