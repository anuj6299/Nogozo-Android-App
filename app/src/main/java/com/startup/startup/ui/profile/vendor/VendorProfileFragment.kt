package com.startup.startup.ui.profile.vendor

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.startup.startup.R
import com.startup.startup.ui.BaseFragment
import com.startup.startup.ui.ViewModelFactory
import com.startup.startup.ui.main.DataResource
import javax.inject.Inject

class VendorProfileFragment: BaseFragment(R.layout.fragment_profile_vendor) {

    @Inject
    lateinit var factory: ViewModelFactory

    private lateinit var viewModel: VendorProfileFragmentViewModel

    private lateinit var shopName: TextView
    private lateinit var progressBar: ProgressBar

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(this, factory)[VendorProfileFragmentViewModel::class.java]

        shopName = view.findViewById(R.id.vendor_profile_shopname)
        progressBar = view.findViewById(R.id.vendor_profile_progressbar)

        getUserProfile()
    }

    private fun getUserProfile(){
        viewModel.getLiveData().removeObservers(viewLifecycleOwner)

        viewModel.getLiveData().observe(viewLifecycleOwner, Observer{
            when(it.status){
                DataResource.Status.LOADING -> {
                    progressBar.visibility = View.VISIBLE
                }
                DataResource.Status.ERROR -> {
                    progressBar.visibility = View.GONE
                }
                DataResource.Status.SUCCESS -> {
                    shopName.text = it.data.shopname
                    progressBar.visibility = View.GONE
                }
            }
        })

        viewModel.getUserProfile()
    }
}