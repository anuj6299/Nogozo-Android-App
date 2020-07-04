package com.startup.startup.ui.main.customer.services

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.DividerDrawerItem
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import com.startup.startup.R
import com.startup.startup.SessionManager
import com.startup.startup.ui.BaseFragment
import com.startup.startup.ui.ViewModelFactory
import com.startup.startup.ui.main.Communicator
import com.startup.startup.ui.main.DataResource
import com.startup.startup.ui.orders.OrdersActivity
import com.startup.startup.ui.profile.ProfileActivity
import com.startup.startup.ui.splash.SplashActivity
import com.startup.startup.util.Constants
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
    private lateinit var drawerButton: ImageButton
    private lateinit var areaButton: MaterialButton

    private lateinit var adapter: ServicesListAdapter
    private var customerDrawer: Drawer? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel = ViewModelProvider(this, factory)[CustomerServiceFragmentViewModel::class.java]

        recyclerView = view.findViewById(R.id.customer_services_recyclerview)
        progressBar = view.findViewById(R.id.fragment_service_progressBar)
        drawerButton = view.findViewById(R.id.service_header_profile)
        drawerButton.setOnClickListener(this)
        areaButton = view.findViewById(R.id.service_header_area)
        areaButton.setOnClickListener(this)
        areaButton.text = sessionManager.getAreaName()

        buildDrawer()
        initRecyceler()
        getServices()
    }

    private fun buildDrawer(){
        //        account header
        customerDrawer = DrawerBuilder()
            .withActivity(activity!!)
            .withTranslucentNavigationBar(true)
            .withDrawerGravity(GravityCompat.END)
            //.withAccountHeader(AccountHeader().)
            .addDrawerItems(
                PrimaryDrawerItem().withIdentifier(0).withName("Orders").withSelectable(false).withTextColor(ContextCompat.getColor(context!!, R.color.colorPrimaryLight)),
                //PrimaryDrawerItem().withIdentifier(1).withName("Current Orders").withSelectable(false).withTextColor(ContextCompat.getColor(this, R.color.colorPrimaryLight)),
                //PrimaryDrawerItem().withIdentifier(2).withName("Past Orders").withSelectable(false).withTextColor(ContextCompat.getColor(this, R.color.colorPrimaryLight)),
                PrimaryDrawerItem().withIdentifier(1).withName("Profile").withSelectable(false).withTextColor(ContextCompat.getColor(context!!, R.color.colorPrimaryLight)),
                DividerDrawerItem().withIdentifier(2),
                SecondaryDrawerItem().withIdentifier(3).withName("Sign Out").withSelectable(false).withTextColor(ContextCompat.getColor(context!!, R.color.red))
            )
            .withOnDrawerItemClickListener(object: Drawer.OnDrawerItemClickListener{
                override fun onItemClick(view: View?, position: Int, drawerItem: IDrawerItem<*>): Boolean {
                    when(position){
                        0 -> {
                            val i = Intent(context, OrdersActivity::class.java)
                            i.putExtra(Constants.USER_TYPE, Constants.userType_CUSTOMER)
                            startActivity(i)
                        }
                        98 -> {
                            val i = Intent(context, OrdersActivity::class.java)
                            i.putExtra(Constants.USER_TYPE, Constants.userType_CUSTOMER)
                            i.putExtra(Constants.ORDER_TYPE, Constants.CURRENT_ORDER)
                            startActivity(i)
                        }
                        99 -> {
                            val i = Intent(context, OrdersActivity::class.java)
                            i.putExtra(Constants.USER_TYPE, Constants.userType_CUSTOMER)
                            i.putExtra(Constants.ORDER_TYPE, Constants.PAST_ORDER)
                            startActivity(i)
                        }
                        1 -> {
                            val i = Intent(context, ProfileActivity::class.java)
                            i.putExtra(Constants.USER_TYPE, Constants.userType_CUSTOMER)
                            startActivity(i)
                        }
                        3 -> {
                            sessionManager.logout()
                            val i = Intent(context, SplashActivity::class.java)
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            startActivity(i)
                        }
                    }
                    customerDrawer?.closeDrawer()
                    return true
                }
            })
            .build()
        customerDrawer?.setSelection(-1, false)
    }

    private fun initRecyceler(){
        recyclerView.layoutManager = GridLayoutManager(context,2)
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
        communicator.onServiceSelected(adapter.getItemAt(position).serviceId, adapter.getItemAt(position).serviceName)
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.service_header_area -> {
                //TODO change current area (for this run only)
            }
            R.id.service_header_profile -> {
                customerDrawer?.openDrawer()
            }
        }
    }
}