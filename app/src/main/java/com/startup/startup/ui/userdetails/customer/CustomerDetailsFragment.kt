package com.startup.startup.ui.userdetails.customer

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.startup.startup.R
import com.startup.startup.ui.BaseFragment
import com.startup.startup.ui.ChooseOnMapActivity
import com.startup.startup.ui.ViewModelFactory
import com.startup.startup.ui.auth.AuthResource
import com.startup.startup.ui.splash.SplashActivity
import com.startup.startup.util.Constants
import com.startup.startup.util.Constants.CHOOSE_ON_MAP_REQUEST_CODE
import javax.inject.Inject

class CustomerDetailsFragment: BaseFragment(R.layout.fragment_userdetails_customer), View.OnClickListener {
    @Inject
    lateinit var factory: ViewModelFactory

    private lateinit var viewModel: CustomerDetailsFragmentViewModel

    private lateinit var addressField: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel = ViewModelProvider(this, factory)[CustomerDetailsFragmentViewModel::class.java]
        addressField = view.findViewById(R.id.customer_userdetails_address_field)
        addressField.setOnClickListener(this)
        view.findViewById<Button>(R.id.customer_userdetails_logout).setOnClickListener(this)

        subscribeObserver()
    }

    private fun subscribeObserver(){
        viewModel.getCurrentUser().removeObservers(viewLifecycleOwner)

        viewModel.getCurrentUser().observe(viewLifecycleOwner, Observer {
            when(it.Status){
                AuthResource.AuthStatus.AUTHENTICATED -> {}
                AuthResource.AuthStatus.LOADING -> {
                    println("UserDetails: Customer: Loading")
                }
                AuthResource.AuthStatus.NOT_AUTHENTICATED -> {
                    val i = Intent(activity, SplashActivity::class.java)
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(i)
                }
                AuthResource.AuthStatus.ERROR -> {}
            }
        })
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.customer_userdetails_address_field -> {
                val i = Intent(activity, ChooseOnMapActivity::class.java)
                startActivityForResult(i, CHOOSE_ON_MAP_REQUEST_CODE)
            }
            R.id.customer_userdetails_logout -> {
                viewModel.logOut()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when(requestCode){
            CHOOSE_ON_MAP_REQUEST_CODE -> {
                if(resultCode == Activity.RESULT_OK)
                addressField.text = data!!.getStringExtra("address")
            }
        }
    }
}