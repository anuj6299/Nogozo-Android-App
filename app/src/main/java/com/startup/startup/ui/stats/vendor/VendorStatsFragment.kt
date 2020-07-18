package com.startup.startup.ui.stats.vendor

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.startup.startup.R
import com.startup.startup.ui.BaseFragment
import com.startup.startup.ui.ViewModelFactory
import com.startup.startup.ui.main.DataResource
import com.startup.startup.ui.stats.StatsAdapter
import com.startup.startup.util.VerticalSpacingItemDecoration
import javax.inject.Inject

class VendorStatsFragment : BaseFragment(R.layout.fragment_stats_vendor) {

    @Inject
    lateinit var factory: ViewModelFactory

    private lateinit var viewModel: VendorStatsFragmentViewModel

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: StatsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel = ViewModelProvider(this, factory)[VendorStatsFragmentViewModel::class.java]

        recyclerView = view.findViewById(R.id.vendor_stats_recyclerview)

        initRecycler()
        subscribeObserver()
        viewModel.getStats()
    }

    private fun initRecycler() {
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.addItemDecoration(VerticalSpacingItemDecoration(16))
        adapter = StatsAdapter()
        recyclerView.adapter = adapter
    }
    private fun subscribeObserver(){
        viewModel.getStatsLiveData().removeObservers(viewLifecycleOwner)
        viewModel.getStatsLiveData().observe(viewLifecycleOwner, Observer{
            when(it.status){
                DataResource.Status.SUCCESS -> {
                    adapter.setData(it.data)
                }
                DataResource.Status.ERROR -> {
                    // show error
                }
                DataResource.Status.LOADING -> {
                    //show progressbar
                }
            }
        })
    }
}