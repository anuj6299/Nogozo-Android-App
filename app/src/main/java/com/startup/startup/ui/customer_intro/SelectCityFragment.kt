package com.startup.startup.ui.customer_intro

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Spinner
import com.startup.startup.R
import dagger.android.support.DaggerFragment

class SelectCityFragment: DaggerFragment(){

    private lateinit var citySpinner:Spinner

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_select_city, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        citySpinner = view.findViewById(R.id.city_spinner)
    }
}