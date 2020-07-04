package com.startup.startup.ui.inventory.inventory

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.startup.startup.R
import com.startup.startup.datamodels.Item
import com.startup.startup.ui.BaseFragment
import com.startup.startup.ui.ViewModelFactory
import com.startup.startup.ui.inventory.InventoryCommunicator
import com.startup.startup.ui.main.Communicator
import com.startup.startup.ui.main.DataResource
import com.startup.startup.util.VerticalSpacingItemDecoration
import javax.inject.Inject

class VendorInventoryFragment(private val communicator: InventoryCommunicator): BaseFragment(R.layout.fragment_inventory),
    InventoryAdapter.OnInventoryItemClick, View.OnClickListener {

    @Inject
    lateinit var factory: ViewModelFactory

    private lateinit var viewModel: VendorInventoryFragmentViewModel
    private lateinit var adapter: InventoryAdapter

    private lateinit var recyclerView: RecyclerView
    private lateinit var fab: FloatingActionButton
    private lateinit var progressBar: ProgressBar
    private lateinit var swipeRefresh: SwipeRefreshLayout

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel = ViewModelProvider(this, factory)[VendorInventoryFragmentViewModel::class.java]

        recyclerView = view.findViewById(R.id.inventory_recyclerview)
        fab = view.findViewById(R.id.inventory_fab)
        fab.setOnClickListener(this)
        progressBar = view.findViewById(R.id.inventory_progressbar)
        swipeRefresh = view.findViewById(R.id.inventory_swiperefresh)
        swipeRefresh.setOnRefreshListener{
            viewModel.getItems()
            swipeRefresh.isRefreshing = false
        }

        initRecycler()
        subscribeObserver()
    }

    private fun initRecycler(){
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.addItemDecoration(VerticalSpacingItemDecoration(16))
        adapter = InventoryAdapter(this)
        recyclerView.adapter = adapter
    }

    private fun subscribeObserver(){
        viewModel.getLiveData().observe(this, Observer{
            when(it.status){
                DataResource.Status.LOADING -> {
                    progressBar.visibility = View.VISIBLE
                }
                DataResource.Status.ERROR -> {
                    progressBar.visibility = View.GONE
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                }
                DataResource.Status.SUCCESS -> {
                    progressBar.visibility = View.GONE
                    adapter.setDataList(it.data)
                }
            }
        })
        viewModel.getItems()
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.inventory_fab -> {
                communicator.onNewItem()
            }
        }
    }

    override fun onInventoryItemClick(item: Item) {
        communicator.onItemClick(item)
    }

    override fun onAvailabililtyChanged(itemId: String, newAvailabilityStatus: String) {
        viewModel.changeItemStatus(itemId, newAvailabilityStatus)
    }
}