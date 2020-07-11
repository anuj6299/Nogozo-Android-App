package com.startup.startup.ui.profile.vendor

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.startup.startup.R
import com.startup.startup.datamodels.VendorProfile
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
    private lateinit var shopOwnerName: TextView
    private lateinit var shopphone: TextView
    private lateinit var shopCity: TextView
    private lateinit var shopArea: TextView
    private lateinit var shopAddress: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(this, factory)[VendorProfileFragmentViewModel::class.java]

        shopName = view.findViewById(R.id.vendor_profile_shopname)
        progressBar = view.findViewById(R.id.vendor_profile_progressbar)
        shopOwnerName = view.findViewById(R.id.vendor_profile_shopOwnerName)
        shopphone = view.findViewById(R.id.vendor_profile_shop_phone)
        shopCity = view.findViewById(R.id.vendor_profile_shop_city)
        shopArea = view.findViewById(R.id.vendor_profile_shop_area)
        shopAddress = view.findViewById(R.id.vendor_profile_shop_address)

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
                    setDatatoView(it.data)
                    progressBar.visibility = View.GONE
                }
            }
        })

        viewModel.getUserProfile()
    }

    private fun setDatatoView(profile: VendorProfile){
        shopName.text = profile.shopname
        shopOwnerName.text = profile.name
        shopphone.text = profile.phone
        shopCity.text = profile.cityname
        shopArea.text = profile.areaname
        shopAddress.text = profile.address
    }
}