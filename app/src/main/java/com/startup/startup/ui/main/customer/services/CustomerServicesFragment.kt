package com.startup.startup.ui.main.customer.services

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.startup.startup.R
import com.startup.startup.SessionManager
import com.startup.startup.ui.BaseFragment
import com.startup.startup.ui.ViewModelFactory
import com.startup.startup.ui.main.Communicator
import com.startup.startup.ui.main.DataResource
import com.startup.startup.util.VerticalSpacingItemDecoration
import javax.inject.Inject

class CustomerServicesFragment(private val communicator: Communicator): BaseFragment(R.layout.fragment_main_customer_services),
    ServicesListAdapter.OnServicesClickInterface, View.OnClickListener {

    @Inject
    lateinit var factory: ViewModelFactory

    @Inject
    lateinit var sessionManager: SessionManager

    private lateinit var viewModel: CustomerServiceFragmentViewModel

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var searchWrapper: View

    private lateinit var adapter: ServicesListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel = ViewModelProvider(this, factory)[CustomerServiceFragmentViewModel::class.java]

        recyclerView = view.findViewById(R.id.customer_services_recyclerview)
        progressBar = view.findViewById(R.id.fragment_service_progressBar)
        searchWrapper = view.findViewById(R.id.fragment_service_search_wrapper)
        searchWrapper.setOnClickListener(this)

        initRecyceler()
        getServices()
    }

    private fun initRecyceler(){
        recyclerView.layoutManager = GridLayoutManager(context,2)
//        recyclerView.layoutManager = StaggeredGridLayoutManager(2, VERTICAL)
        recyclerView.addItemDecoration(VerticalSpacingItemDecoration(16))
        adapter = ServicesListAdapter(this)
        recyclerView.adapter = adapter
    }

    private fun getServices(){
        viewModel.getLiveData().removeObservers(viewLifecycleOwner)

        viewModel.getLiveData().observe(viewLifecycleOwner, Observer {
            when(it.status){
                DataResource.Status.SUCCESS -> {
                    adapter.setData(it.data)
                    progressBar.visibility = View.GONE
                }
                DataResource.Status.LOADING -> {
                    progressBar.visibility = View.VISIBLE
                }
                DataResource.Status.ERROR -> {
                    progressBar.visibility = View.GONE
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        })

        viewModel.getServices()
    }

    override fun onServiceClick(position: Int) {
        communicator.onServiceSelected(adapter.getItemAt(position).serviceId!!, adapter.getItemAt(position).servicename!!)
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.fragment_service_search_wrapper -> {
                communicator.onGlobalSearch()
            }
        }
    }
}