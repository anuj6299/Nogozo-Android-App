package com.startup.startup.ui.main.customer.shops

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.startup.startup.R
import com.startup.startup.ui.BaseFragment
import com.startup.startup.ui.ViewModelFactory
import com.startup.startup.ui.main.Communicator
import com.startup.startup.ui.main.DataResource
import com.startup.startup.util.VerticalSpacingItemDecoration
import javax.inject.Inject

class ShopListFragment(val communicator: Communicator): BaseFragment(R.layout.fragment_main_shops), ShopListAdapter.OnShopClickInterface{

    @Inject
    lateinit var factory: ViewModelFactory

    lateinit var viewModel: ShopListFragmentViewModel

    lateinit var recyclerView: RecyclerView
    lateinit var progressBar: ProgressBar

    lateinit var adapter: ShopListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        communicator.setToolbarTitle("Shops")

        viewModel = ViewModelProvider(this, factory)[ShopListFragmentViewModel::class.java]

        recyclerView = view.findViewById(R.id.fragment_main_shop_recyclerview)
        progressBar = view.findViewById(R.id.fragment_shops_progressBar)

        initRecycler()

        getShops(arguments!!.getString("serviceid","-1"))

    }

    private fun initRecycler(){
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.addItemDecoration(VerticalSpacingItemDecoration(16))
        adapter = ShopListAdapter(this)
        recyclerView.adapter = adapter
    }

    private fun getShops(serviceId: String){
        viewModel.getShopsList(serviceId).removeObservers(viewLifecycleOwner)

        viewModel.getShopsList(serviceId).observe(viewLifecycleOwner, Observer {
            when(it!!.status){
                DataResource.Status.SUCCESS -> {
                    progressBar.visibility = View.GONE
                    adapter.setItemList(it.data)
                }
                DataResource.Status.ERROR -> {
                    Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                }
                DataResource.Status.LOADING -> {
                    progressBar.visibility = View.VISIBLE
                }
            }
        })
    }

    override fun onShopClick(position: Int) {
        communicator.onShopSelected(adapter.getItemAt(position).shopId, adapter.getItemAt(position).shopName!!)
    }
}