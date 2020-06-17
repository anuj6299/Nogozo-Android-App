package com.startup.startup.ui.main.customer

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.startup.startup.R
import com.startup.startup.ui.BaseFragment
import com.startup.startup.ui.ViewModelFactory
import com.startup.startup.util.VerticalSpacingItemDecoration
import javax.inject.Inject

class CustomerServicesFragment: BaseFragment(R.layout.fragment_services_customer), ServicesListAdapter.OnServicesClickInterface {

    @Inject
    lateinit var factory: ViewModelFactory

    private lateinit var viewModel: CustomerServiceFragmentViewModel

    private lateinit var recyclerView: RecyclerView

    private lateinit var adapter: ServicesListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(this, factory)[CustomerServiceFragmentViewModel::class.java]

        recyclerView = view.findViewById(R.id.customer_services_recyclerview)
        initRecyceler()
        getServices()
    }

    private fun initRecyceler(){
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.addItemDecoration(VerticalSpacingItemDecoration(16))
        adapter = ServicesListAdapter(this)
        recyclerView.adapter = adapter
    }

    private fun getServices(){
        viewModel.getServices().removeObservers(viewLifecycleOwner)

        viewModel.getServices().observe(viewLifecycleOwner, Observer {
            when(it.status){
                ServiceResource.Status.SUCCESS -> {
                    adapter.setData(it.data)
                }
                ServiceResource.Status.LOADING -> {
                    println("servicesFragment LOADING")
                }
                ServiceResource.Status.ERROR -> {
                    println("servicesFragment ${it.message}")
                }
            }
        })
    }

    override fun onServiceClick(position: Int) {
        Toast.makeText(context, "${adapter.getItemAt(position).serviceName} Clicked", Toast.LENGTH_SHORT).show()
    }
}