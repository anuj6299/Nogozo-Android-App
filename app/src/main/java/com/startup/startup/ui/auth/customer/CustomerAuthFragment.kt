package com.startup.startup.ui.auth.customer

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.lifecycle.ViewModelProvider
import androidx.transition.TransitionManager
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.startup.startup.R
import com.startup.startup.datamodels.CustomerProfile
import com.startup.startup.ui.BaseFragment
import com.startup.startup.ui.ViewModelFactory
import com.startup.startup.ui.auth.AuthResource
import com.startup.startup.ui.main.MainActivity
import com.startup.startup.ui.userdetails.UserDetailsActivity
import com.startup.startup.util.Constants.PROFILE_LEVEL_0
import com.startup.startup.util.Constants.PROFILE_LEVEL_1
import com.startup.startup.util.Constants.USER_TYPE
import com.startup.startup.util.Constants.userType_CUSTOMER
import com.startup.startup.util.Helper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CustomerAuthFragment: BaseFragment(R.layout.fragment_auth_customer_signin), View.OnClickListener {

    @Inject
    lateinit var factory: ViewModelFactory

    private lateinit var viewModel: CustomerAuthFragmentViewModel

    private val viewType_SIGNUP = 0
    private val viewType_SIGNIN = 1
    private var viewType = viewType_SIGNIN

    //private lateinit var nameField: TextInputEditText
    private lateinit var emailField: TextInputEditText
    private lateinit var passwordField: TextInputEditText
    private lateinit var signinButton: MaterialButton
    private lateinit var signupButton: MaterialButton
    private lateinit var progressBar: ProgressBar
    private lateinit var cc1: ConstraintLayout

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //nameField = view.findViewById(R.id.customer_name_field)
        emailField = view.findViewById(R.id.customer_email_field)
        passwordField = view.findViewById(R.id.customer_password_field)
        progressBar = view.findViewById(R.id.auth_customer_progressBar)
        signinButton = view.findViewById(R.id.customer_login_button)
        signinButton.setBackgroundColor(resources.getColor(R.color.colorPrimary, resources.newTheme()))
        signinButton.setOnClickListener(this)
        signupButton = view.findViewById(R.id.customer_signup_button)
        signupButton.setOnClickListener(this)
        cc1 = view.findViewById(R.id.customer_auth_signin)

        viewModel = ViewModelProvider(this, factory)[CustomerAuthFragmentViewModel::class.java]
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.customer_login_button -> {
                if(viewType == viewType_SIGNIN)
                    login()
                else if(viewType == viewType_SIGNUP)
                    animate()
            }
            R.id.customer_signup_button -> {
                if(viewType == viewType_SIGNUP)
                    register()
                else if(viewType == viewType_SIGNIN)
                    animate()
            }
        }
    }

    private fun login(){
        progressBar.visibility = View.VISIBLE
        CoroutineScope(IO).launch{
            val email = emailField.text.toString()
            val password = passwordField.text.toString()
            if(Helper.isValidEmail(email)){
                if(password.length > 6){
                    val task = viewModel.login(emailField.text.toString(), passwordField.text.toString())
                    task.addOnCompleteListener{
                        if(it.isSuccessful){
                            viewModel.saveOnLogged(email)
                            viewModel.getUserProfile()
                                .addListenerForSingleValueEvent(object: ValueEventListener{
                                    override fun onCancelled(error: DatabaseError) {
                                        showToast(error.message)
                                        progressBar.visibility = View.GONE
                                        showToast("Something Went Wrong")
                                    }

                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        val customerProfile = snapshot.getValue<CustomerProfile>()
                                        if(customerProfile != null){
                                            if(customerProfile.profilelevel == PROFILE_LEVEL_0){
                                                val i = Intent(context, UserDetailsActivity::class.java)
                                                i.putExtra(USER_TYPE, userType_CUSTOMER)
                                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                                startActivity(i)
                                            }
                                            else if(customerProfile.profilelevel == PROFILE_LEVEL_1){
                                                viewModel.saveProfileToLocal(customerProfile)
                                                val i = Intent(context, MainActivity::class.java)
                                                i.putExtra(USER_TYPE, userType_CUSTOMER)
                                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                                startActivity(i)
                                            }
                                        }else{
                                            val i = Intent(context, UserDetailsActivity::class.java)
                                            i.putExtra(USER_TYPE, userType_CUSTOMER)
                                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                            startActivity(i)
                                        }
                                    }
                                })
                        }else{
                            progressBar.visibility = View.GONE
                            showToast(it.exception!!.message!!)
                        }
                    }
                }else{
                    withContext(Main){
                        progressBar.visibility = View.GONE
                        showToast("Please Enter at least 6 digit password")
                    }
                }
            }else{
                withContext(Main){
                    progressBar.visibility = View.GONE
                    showToast("Please Enter Valid Email")
                }
            }
        }
    }

    private fun register(){
        progressBar.visibility = View.VISIBLE
        CoroutineScope(IO).launch{
            val email = emailField.text.toString()
            val password = passwordField.text.toString()
            if(Helper.isValidEmail(email)){
                if(password.length > 6){
                    val task = viewModel.register(email, password)
                    task.addOnCompleteListener{
                        if(task.isSuccessful){
                            viewModel.saveOnRegistered(email)
                            val i = Intent(context, UserDetailsActivity::class.java)
                            i.putExtra(USER_TYPE, userType_CUSTOMER)
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            startActivity(i)
                        }else{
                            showToast(it.exception!!.message!!)
                            progressBar.visibility = View.GONE
                        }
                    }
                }else{
                    withContext(Main){
                        showToast("Please Enter at least 6 digit password")
                        progressBar.visibility = View.GONE
                    }
                }
            }else{
                withContext(Main){
                    showToast("Please Enter Valid Email")
                    progressBar.visibility = View.GONE
                }
            }
        }
    }

    private fun showToast(msg: String){
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
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
                signupButton.setBackgroundColor(resources.getColor(R.color.colorAccent, resources.newTheme()))
                signinButton.text = "Login"
                signinButton.setTextColor(resources.getColor(R.color.colorAccent, resources.newTheme()))
                signinButton.setBackgroundColor(resources.getColor(R.color.colorPrimary, resources.newTheme()))
                constraintSet1
            }else{
                signupButton.text = "Register"
                signupButton.setTextColor(resources.getColor(R.color.colorAccent, resources.newTheme()))
                signupButton.setBackgroundColor(resources.getColor(R.color.colorPrimary, resources.newTheme()))
                signinButton.text = "Already Have Account? Login"
                signinButton.setTextColor(resources.getColor(R.color.colorPrimary, resources.newTheme()))
                signinButton.setBackgroundColor(resources.getColor(R.color.colorAccent, resources.newTheme()))
                constraintSet2
            }
        constraint.applyTo(cc1)

    }
}