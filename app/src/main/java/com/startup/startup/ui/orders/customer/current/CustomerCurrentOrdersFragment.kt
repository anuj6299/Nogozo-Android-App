package com.startup.startup.ui.orders.customer.current

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.startup.startup.R
import com.startup.startup.ui.BaseFragment
import com.startup.startup.ui.ViewModelFactory
import com.startup.startup.ui.main.DataResource
import com.startup.startup.ui.main.vendor.orders.OrderAdapter
import com.startup.startup.util.VerticalSpacingItemDecoration
import javax.inject.Inject

class CustomerCurrentOrdersFragment: BaseFragment(R.layout.fragment_orders) {

    @Inject
    lateinit var factory: ViewModelFactory

    private lateinit var recyclerView: RecyclerView
    private lateinit var header: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var adapter: OrderAdapter
    private lateinit var swipeRefresh: SwipeRefreshLayout

    private lateinit var viewModel: CustomerCurrentOrdersFragmentViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel = ViewModelProvider(this, factory)[CustomerCurrentOrdersFragmentViewModel::class.java]

        progressBar = view.findViewById(R.id.customer_order_progressbar)
        recyclerView = view.findViewById(R.id.customer_order_recyclerView)
        swipeRefresh = view.findViewById(R.id.customer_order_swipeRefresh)
        //header = view.findViewById(R.id.customer_order_header)
        //header.text = "Current Orders"

        swipeRefresh.setOnRefreshListener{
            swipeRefresh.isRefreshing = false
            viewModel.getCurrentOrderOrders()
        }

        initRecycler()
        getOrders()
    }

    private fun initRecycler(){
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.addItemDecoration(VerticalSpacingItemDecoration(16))
        adapter = OrderAdapter()
        recyclerView.adapter = adapter
    }

    private fun getOrders(){
        viewModel.getLiveData().removeObservers(viewLifecycleOwner)

        viewModel.getLiveData().observe(viewLifecycleOwner, Observer {
            when(it.status){
                DataResource.Status.LOADING -> {
                    progressBar.visibility = View.VISIBLE
                }
                DataResource.Status.SUCCESS -> {
                    progressBar.visibility = View.GONE
                    adapter.setList(it.data)
                }
                DataResource.Status.ERROR -> {
                    progressBar.visibility = View.GONE
                    showToast(it.message)
                }
            }
        })

        viewModel.getCurrentOrderOrders()
    }

    private fun showToast(msg: String){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

}