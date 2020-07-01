package com.startup.startup.ui.main.customer.itemsInShop

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.startup.startup.R
import com.startup.startup.ui.BaseFragment
import com.startup.startup.ui.ViewModelFactory
import com.startup.startup.ui.main.Communicator
import com.startup.startup.ui.main.DataResource
import com.startup.startup.ui.payment.ConfirmActivity
import com.startup.startup.util.Constants.SHOP_ID
import com.startup.startup.util.Constants.SHOP_NAME
import com.startup.startup.util.Constants.USER_TYPE
import com.startup.startup.util.Constants.userType_CUSTOMER
import com.startup.startup.util.VerticalSpacingItemDecoration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ItemsInShopFragment(private val communicator: Communicator): BaseFragment(R.layout.fragment_main_itemsinshop), View.OnClickListener {

    @Inject
    lateinit var factory: ViewModelFactory

    lateinit var viewModel: ItemsInShopFragmentViewModel

    private lateinit var recyclerView: RecyclerView
    private lateinit var proceedButton: MaterialButton
    private lateinit var progressBar: ProgressBar

    private lateinit var adapter: ItemsInShopAdapter
    private lateinit var shopId: String
    private lateinit var shopName: String
    private lateinit var price: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel = ViewModelProvider(this, factory)[ItemsInShopFragmentViewModel::class.java]

        recyclerView = view.findViewById(R.id.fragment_main_itemsinshop_recyclerview)
        progressBar = view.findViewById(R.id.fragment_iteminshops_progressBar)
        proceedButton = view.findViewById(R.id.fragment_main_iteminshop_proceed)
        proceedButton.setOnClickListener(this)

        initRecycler()

        shopId = arguments!!.getString(SHOP_ID,"-1")
        shopName = arguments!!.getString(SHOP_NAME,"-1")

        communicator.setToolbarTitle(shopName)

        getItems(shopId)

        subscribeObserver()
    }

    private fun initRecycler(){
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.addItemDecoration(VerticalSpacingItemDecoration(16))
        adapter = ItemsInShopAdapter()
        recyclerView.adapter = adapter
    }

    private fun subscribeObserver(){
        adapter.getPriceLiveData().removeObservers(viewLifecycleOwner)
        adapter.getPriceLiveData().observe(this, Observer {
            if(it == 0){
                proceedButton.visibility = View.INVISIBLE
            }else{
                proceedButton.visibility = View.VISIBLE
                proceedButton.text = "Total Price: ₹$it"
            }
            price = it.toString()
        })
    }

    fun getItems(shopId: String){
        viewModel.getItems().removeObservers(viewLifecycleOwner)

        viewModel.getItems(shopId).observe(viewLifecycleOwner, Observer{
            when(it.status){
                DataResource.Status.SUCCESS -> {
                    progressBar.visibility = View.GONE
                    adapter.setData(it.data)
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
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.fragment_main_iteminshop_proceed -> {
                CoroutineScope(Dispatchers.Default).launch{
                    val map = adapter.getSelectedItem()
                    withContext(Main){
                        val i = Intent(context, ConfirmActivity::class.java)
                        i.putExtra(USER_TYPE, userType_CUSTOMER)
                        val orderData: HashMap<String, Any> = HashMap()
                        orderData["shopid"] = shopId
                        orderData["shopname"] = shopName
                        orderData["price"] = price
                        orderData["status"] = "0"
                        orderData["items"] = map
                        i.putExtra("order", orderData)
                        startActivity(i)
                    }
                }
            }
        }
    }
}