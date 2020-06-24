package com.startup.startup.ui.main.customer.itemsInShop

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
import com.startup.startup.ui.main.DataResource
import com.startup.startup.ui.main.customer.shops.ShopListAdapter
import com.startup.startup.util.Constants.SHOP_ID
import com.startup.startup.util.VerticalSpacingItemDecoration
import javax.inject.Inject

class ItemsInShopFragment: BaseFragment(R.layout.fragment_main_itemsinshop) {

    @Inject
    lateinit var factory: ViewModelFactory

    lateinit var viewModel: ItemsInShopFragmentViewModel

    lateinit var recyclerView: RecyclerView

    lateinit var adapter: ItemsInShopAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel = ViewModelProvider(this, factory)[ItemsInShopFragmentViewModel::class.java]

        recyclerView = view.findViewById(R.id.fragment_main_itemsinshop_recyclerview)

        initRecycler()

        getItems(arguments!!.getString(SHOP_ID,"-1"))
    }

    private fun initRecycler(){
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.addItemDecoration(VerticalSpacingItemDecoration(16))
        adapter = ItemsInShopAdapter()
        recyclerView.adapter = adapter
    }

    fun getItems(shopId: String){
        viewModel.getItems().removeObservers(viewLifecycleOwner)

        viewModel.getItems(shopId).observe(viewLifecycleOwner, Observer{
            when(it.status){
                DataResource.Status.SUCCESS -> {
                    adapter.setData(it.data)
                }
                DataResource.Status.LOADING -> {}
                DataResource.Status.ERROR -> {
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}