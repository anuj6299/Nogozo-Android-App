package com.startup.startup.ui.userdetails.customer

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.SearchView
import androidx.cardview.widget.CardView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.button.MaterialButton
import com.startup.startup.R
import com.startup.startup.datamodels.Area
import com.startup.startup.datamodels.City
import com.startup.startup.ui.BaseFragment
import com.startup.startup.ui.ChooseOnMapActivity
import com.startup.startup.ui.ViewModelFactory
import com.startup.startup.ui.auth.AuthResource
import com.startup.startup.ui.main.MainActivity
import com.startup.startup.ui.splash.SplashActivity
import com.startup.startup.ui.userdetails.AreaListAdapter
import com.startup.startup.ui.userdetails.CityListAdapter
import com.startup.startup.ui.userdetails.CityResource
import com.startup.startup.util.Constants.CHOOSE_ON_MAP_REQUEST_CODE
import com.startup.startup.util.Constants.DIALOG_TYPE_AREA
import com.startup.startup.util.Constants.DIALOG_TYPE_CITY
import com.startup.startup.util.Constants.ERROR
import com.startup.startup.util.Constants.LOADING
import com.startup.startup.util.Constants.PROFILE_LEVEL_1
import com.startup.startup.util.Constants.SUCCESS
import com.startup.startup.util.Constants.USER_TYPE
import com.startup.startup.util.Constants.userType_CUSTOMER
import javax.inject.Inject


class CustomerDetailsFragment: BaseFragment(R.layout.fragment_userdetails_customer), View.OnClickListener {
    @Inject
    lateinit var factory: ViewModelFactory

    private lateinit var viewModel: CustomerDetailsFragmentViewModel

    private lateinit var addressField: TextView
    private lateinit var citySpinner: TextView
    private lateinit var areaSpinner: TextView
    private lateinit var cityCard: CardView
    private lateinit var areaCard: CardView
    private lateinit var confirmButton: MaterialButton
    private var selectedCity: City? = null
    private var selectedArea: Area? = null
    private var selectedAddress: String? = null
    private var selectedLat: Double? = null
    private var selectedLon: Double? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel = ViewModelProvider(this, factory)[CustomerDetailsFragmentViewModel::class.java]
        addressField = view.findViewById(R.id.customer_userdetails_address_field)
        addressField.setOnClickListener(this)
        view.findViewById<Button>(R.id.customer_userdetails_logout).setOnClickListener(this)

        areaCard = view.findViewById(R.id.customer_userdetails_area_wrapper)
        cityCard = view.findViewById(R.id.customer_userdetails_city_wrapper)
        citySpinner = view.findViewById(R.id.customer_userdetails_city_view)
        citySpinner.setOnClickListener(this)
        areaSpinner = view.findViewById(R.id.customer_userdetails_area_view)
        areaSpinner.setOnClickListener(this)
        confirmButton = view.findViewById(R.id.customer_userdetails_confirm_button)
        confirmButton.setOnClickListener(this)

        subscribeObserver()
    }

    private fun subscribeObserver(){
        viewModel.getCurrentUser().removeObservers(viewLifecycleOwner)

        viewModel.getCurrentUser().observe(viewLifecycleOwner, Observer {
            when(it.Status){
                AuthResource.AuthStatus.AUTHENTICATED -> {
                    if(it.data.profileLevel == PROFILE_LEVEL_1){
                        val i = Intent(activity, MainActivity::class.java)
                        i.putExtra(USER_TYPE, userType_CUSTOMER)
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        startActivity(i)
                    }
                }
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
            R.id.customer_userdetails_city_view -> {
                openDialogForSelecting(DIALOG_TYPE_CITY)
            }
            R.id.customer_userdetails_area_view -> {
                openDialogForSelecting(DIALOG_TYPE_AREA, selectedCity!!.cityId)
            }
            R.id.customer_userdetails_confirm_button -> {
                updateUserProfile()
                //Toast.makeText(context, "Coming Soon", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun onCitySelected(city: City){
        if(selectedCity != null){
            if(selectedCity!!.cityName == city.cityName && selectedCity!!.cityId == city.cityId){
                return
            }
        }
        selectedCity = city
        citySpinner.text = city.cityName
        selectedArea = null
        areaSpinner.text= "Select Your Area"
        areaCard.visibility = View.VISIBLE
        checkAndShowButton()
    }

    private fun onAreaSelected(area: Area){
        selectedArea = area
        areaSpinner.text = area.areaName
        addressField.visibility = View.VISIBLE
        checkAndShowButton()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when(requestCode){
            CHOOSE_ON_MAP_REQUEST_CODE -> {
                if(resultCode == Activity.RESULT_OK){
                    selectedAddress = data!!.getStringExtra("address")
                    addressField.text = selectedAddress
                    selectedLat = data.getDoubleExtra("lat", 0.0)
                    selectedLon = data.getDoubleExtra("lon", 0.0)
                    checkAndShowButton()
                }
            }
        }
    }

    private fun openDialogForSelecting(type: String, cityid: String = ""){
        val dialog = Dialog(context!!)
        dialog.setContentView(R.layout.dialog_select_from_list)

        val searchView: SearchView = dialog.findViewById(R.id.dialog_searchview)
        val listView: ListView = dialog.findViewById(R.id.dialog_listview)
        val progressBar: ProgressBar = dialog.findViewById(R.id.dialog_progressbar)

        if(type == DIALOG_TYPE_CITY){
            viewModel.getCities().removeObservers(viewLifecycleOwner)

            viewModel.getCities().observe(viewLifecycleOwner, Observer {
                when(it.status){
                    CityResource.CityStatus.SUCCESS -> {
                        progressBar.visibility = View.INVISIBLE

                        searchView.setQuery("",false)
                        val adapter = CityListAdapter()
                        listView.adapter = adapter
                        adapter.setOriginalList(it.data)

                        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
                            override fun onQueryTextSubmit(query: String?): Boolean {
                                return false
                            }

                            override fun onQueryTextChange(newText: String?): Boolean {
                                adapter.getFilter().filter(newText)
                                return true
                            }
                        })

                        listView.onItemClickListener = AdapterView.OnItemClickListener{ parent, view, position, id ->
                            if(adapter.getItem(position).cityId != "-1"){
                                onCitySelected(adapter.getItem(position))
                                dialog.dismiss()
                            }
                        }

                        println("userDetails: success ${it.data}")
                    }
                    CityResource.CityStatus.LOADING -> {
                        println("userDetails: loading")

                    }
                    CityResource.CityStatus.ERROR -> {
                        progressBar.visibility = View.INVISIBLE
                        println("userDetails: error")
                    }
                }
            })
        }else if(type == DIALOG_TYPE_AREA){
            viewModel.getAreaOfCity(cityid).removeObservers(viewLifecycleOwner)

            viewModel.getAreaOfCity(cityid).observe(viewLifecycleOwner, Observer {
                when(it.status){
                    CityResource.CityStatus.SUCCESS -> {
                        progressBar.visibility = View.INVISIBLE

                        searchView.setQuery("",false)
                        val adapter = AreaListAdapter()
                        listView.adapter = adapter
                        adapter.setOriginalList(it.data.areas)

                        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
                            override fun onQueryTextSubmit(query: String?): Boolean {
                                return false
                            }

                            override fun onQueryTextChange(newText: String?): Boolean {
                                adapter.getFilter().filter(newText)
                                return true
                            }
                        })

                        listView.onItemClickListener = AdapterView.OnItemClickListener{ parent, view, position, id ->
                            if(adapter.getItem(position).areaId != "-1"){
                                onAreaSelected(adapter.getItem(position))
                                dialog.dismiss()
                            }
                        }

                        println("userDetails: success ${it.data}")
                    }
                    CityResource.CityStatus.LOADING -> {
                        println("userDetails: loading")

                    }
                    CityResource.CityStatus.ERROR -> {
                        progressBar.visibility = View.INVISIBLE
                        println("userDetails: error")
                    }
                }
            })
        }

        dialog.show()
        dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }

    private fun checkAndShowButton(){
        if(selectedCity != null && selectedArea != null && selectedAddress != null){
            confirmButton.visibility = View.VISIBLE
        }else{
            confirmButton.visibility = View.INVISIBLE
        }
    }

    private fun updateUserProfile(){
        viewModel.updateUserProfile(
            selectedCity!!.cityId,
            selectedArea!!.areaId,
            selectedAddress!!,
            selectedLat!!,
            selectedLon!!
        ).removeObservers(viewLifecycleOwner)

        viewModel.updateUserProfile(selectedCity!!.cityId,
            selectedArea!!.areaId,
            selectedAddress!!,
            selectedLat!!,
            selectedLon!!
        ).observe(viewLifecycleOwner, Observer {
            when(it!!){
                LOADING -> {
                    println("customer: UserDetails: UpdateUser: LOADING")
                }
                SUCCESS -> {
                    val i = Intent(activity, MainActivity::class.java)
                    i.putExtra(USER_TYPE, userType_CUSTOMER)
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(i)
                }
                ERROR -> {
                    println("customer: UserDetails: UpdateUser: ERROR")
                }
            }
        })
    }
}