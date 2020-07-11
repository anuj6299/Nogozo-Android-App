package com.startup.startup.ui.main.vendor.orders.current

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.startup.startup.R
import com.startup.startup.SessionManager
import com.startup.startup.ui.BaseFragment
import com.startup.startup.ui.ViewModelFactory
import com.startup.startup.ui.main.Communicator
import com.startup.startup.ui.main.DataResource
import com.startup.startup.ui.main.vendor.orders.OrderAdapter
import com.startup.startup.util.OrderByStatus
import com.startup.startup.util.VerticalSpacingItemDecoration
import javax.inject.Inject

class VendorCurrentOrdersFragment(
    private val communicator: Communicator
): BaseFragment(R.layout.fragment_main_currentorder_vendor),
    View.OnClickListener{

    @Inject
    lateinit var factory: ViewModelFactory
    @Inject
    lateinit var sessionManager: SessionManager

    lateinit var viewModel: VendorCurrentOrdersFragmentViewModel
    private lateinit var adapter: OrderAdapter
    private var shopStatus: String = ""

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var swipeRefresh: SwipeRefreshLayout

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel = ViewModelProvider(this, factory)[VendorCurrentOrdersFragmentViewModel::class.java]

        recyclerView = view.findViewById(R.id.currentorder_vendor_recyclerview)
        progressBar = view.findViewById(R.id.fragment_currentorder_vendor_progressBar)
        swipeRefresh = view.findViewById(R.id.currentorder_vendor_swipeRefresh)

        initRecycler()

        swipeRefresh.setOnRefreshListener{
            swipeRefresh.isRefreshing = false
            viewModel.getCurrentOrders()
        }

        getCurrentOrders()
        getCurrentShopStatus()
    }

    private fun initRecycler(){
        adapter = OrderAdapter(true, OrderByStatus())
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.addItemDecoration(VerticalSpacingItemDecoration(16))
        recyclerView.adapter = adapter
    }

    private fun getCurrentOrders(){
        viewModel.getLiveData().removeObservers(viewLifecycleOwner)

        viewModel.getLiveData().observe(viewLifecycleOwner, Observer {
            when(it.status){
                DataResource.Status.SUCCESS -> {
                    progressBar.visibility = View.GONE
                    adapter.setList(it.data)
                }
                DataResource.Status.ERROR -> {
                    progressBar.visibility = View.GONE
                    Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                }
                DataResource.Status.LOADING -> {
                    progressBar.visibility = View.VISIBLE
                }
            }
        })

        viewModel.getCurrentOrders()
    }

    private fun getCurrentShopStatus(){
        viewModel.getStatusLiveData().removeObservers(viewLifecycleOwner)
        viewModel.getStatusLiveData().observe(viewLifecycleOwner, Observer{
            if(it == "OPEN"){
                shopStatus = "OPEN"

            }
            else if(it == "CLOSED"){
                shopStatus = "CLOSED"

            }
        })
        viewModel.getCurrentShopStatus()
    }

    override fun onClick(v: View?) {
//        when(v!!.id){
//            R.id.vendor_main_header_profile -> {
//                shopDrawer?.openDrawer()
//            }
//            R.id.vendor_main_header_shopstatus -> {
//                var status = ""
//                if(shopStatus == "OPEN")
//                    status = "CLOSED"
//                else
//                    status = "OPEN"
//                viewModel.changeShopStatus(status).addOnCompleteListener{
//                    if(it.isSuccessful){
//                        Toast.makeText(context, "Your Shop is now $status", Toast.LENGTH_SHORT).show()
//                    }
//                }
//            }
//        }
    }
}