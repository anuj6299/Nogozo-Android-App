package com.startup.startup.ui.main.customer.shops

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
import com.startup.startup.SessionManager
import com.startup.startup.datamodels.Services
import com.startup.startup.ui.BaseFragment
import com.startup.startup.ui.ViewModelFactory
import com.startup.startup.ui.main.Communicator
import com.startup.startup.ui.main.DataResource
import com.startup.startup.util.Constants.SERVICE_ID
import com.startup.startup.util.Constants.SERVICE_NAME
import com.startup.startup.util.VerticalSpacingItemDecoration
import kotlinx.android.synthetic.main.fragment_main_shops.*
import javax.inject.Inject

class ShopListFragment(
    private val communicator: Communicator
): BaseFragment(R.layout.fragment_main_shops), ShopListAdapter.OnShopClickInterface{

    @Inject
    lateinit var factory: ViewModelFactory
    @Inject
    lateinit var sessionManager: SessionManager

    private lateinit var viewModel: ShopListFragmentViewModel

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var shopNameHeader: TextView
    private lateinit var swipeRefresh: SwipeRefreshLayout

    private lateinit var adapter: ShopListAdapter

    private var service: Services? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel = ViewModelProvider(this, factory)[ShopListFragmentViewModel::class.java]

        recyclerView = view.findViewById(R.id.fragment_main_shop_recyclerview)
        progressBar = view.findViewById(R.id.fragment_shops_progressBar)
        shopNameHeader = view.findViewById(R.id.shop_header_name)
        swipeRefresh = view.findViewById(R.id.fragment_shops_swipeRefresh)

        initRecycler()

        service = Services(arguments!!.getString(SERVICE_ID,"-1"), arguments!!.getString(SERVICE_NAME,"-1"), "")

        swipeRefresh.setOnRefreshListener{
            swipeRefresh.isRefreshing = false
            viewModel.getShopsList(service?.serviceId!!)
        }

        getShops(service?.serviceId!!)
        shop_header_name.text = "${service!!.serviceName} Shops in ${sessionManager.getAreaName()}"

    }

    private fun initRecycler(){
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.addItemDecoration(VerticalSpacingItemDecoration(16))
        adapter = ShopListAdapter(this)
        recyclerView.adapter = adapter
    }

    private fun getShops(serviceId: String){
        viewModel.getShopLiveData().removeObservers(viewLifecycleOwner)

        viewModel.getShopLiveData().observe(viewLifecycleOwner, Observer {
            when(it!!.status){
                DataResource.Status.SUCCESS -> {
                    progressBar.visibility = View.GONE
                    adapter.setItemList(it.data)
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
        viewModel.getShopsList(serviceId)
    }

    override fun onShopClick(position: Int) {
        communicator.onShopSelected(adapter.getItemAt(position).shopId, adapter.getItemAt(position).shopName, adapter.getItemAt(position).shopAddress)
    }
}