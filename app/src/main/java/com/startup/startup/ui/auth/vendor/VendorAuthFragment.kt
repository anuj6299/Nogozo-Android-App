package com.startup.startup.ui.auth.vendor

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.startup.startup.R
import com.startup.startup.ui.BaseFragment
import com.startup.startup.ui.ViewModelFactory
import com.startup.startup.ui.auth.AuthResource
import com.startup.startup.util.Constants
import javax.inject.Inject

class VendorAuthFragment: BaseFragment(R.layout.fragment_auth_vendor),View.OnClickListener {

    @Inject
    lateinit var factory: ViewModelFactory

    private lateinit var viewModel: VendorAuthFragmentViewModel

    private lateinit var emailField: TextInputEditText
    private lateinit var passwordField: TextInputEditText
    private lateinit var loginButton: MaterialButton


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        emailField = view.findViewById(R.id.vendor_email_field)
        passwordField = view.findViewById(R.id.vendor_password_field)
        loginButton = view.findViewById(R.id.vendor_login_button)
        loginButton.setOnClickListener(this)

        viewModel = ViewModelProvider(this, factory)[VendorAuthFragmentViewModel::class.java]

        viewModel.getCachedUser().removeObservers(viewLifecycleOwner)

        viewModel.getCachedUser().observe(viewLifecycleOwner, Observer{
            when(it.Status){
                AuthResource.AuthStatus.AUTHENTICATED -> {
                    //to next activity on user status
                    println("authenticated")
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
                viewModel.authenticateUser(emailField.text.toString(), passwordField.text.toString(), Constants.userType_VENDOR
                )
            }
        }
    }
}