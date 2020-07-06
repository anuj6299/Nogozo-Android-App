package com.startup.startup.ui.main.vendor.orders.current

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
import com.startup.startup.ui.inventory.VendorInventoryActivity
import com.startup.startup.ui.main.Communicator
import com.startup.startup.ui.main.DataResource
import com.startup.startup.ui.main.vendor.orders.OrderAdapter
import com.startup.startup.ui.orders.OrdersActivity
import com.startup.startup.ui.profile.ProfileActivity
import com.startup.startup.ui.splash.SplashActivity
import com.startup.startup.util.Constants.USER_TYPE
import com.startup.startup.util.Constants.userType_VENDOR
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
    private var shopDrawer: Drawer? = null

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var headerProfile: ImageView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel = ViewModelProvider(this, factory)[VendorCurrentOrdersFragmentViewModel::class.java]

        recyclerView = view.findViewById(R.id.currentorder_vendor_recyclerview)
        progressBar = view.findViewById(R.id.fragment_currentorder_vendor_progressBar)
        headerProfile = view.findViewById(R.id.vendor_main_header_profile)
        headerProfile.setOnClickListener(this)

        buildDrawer()
        initRecycler()

        getCurrentOrders()
    }

    private fun buildDrawer(){
        shopDrawer = DrawerBuilder()
            .withActivity(activity!!)
            .withTranslucentStatusBar(true)
            .withDrawerGravity(GravityCompat.END)
            .addDrawerItems(
                PrimaryDrawerItem().withIdentifier(0).withName("Inventory").withSelectable(false).withTextColor(ContextCompat.getColor(context!!, R.color.colorPrimaryLight)),
                PrimaryDrawerItem().withIdentifier(1).withName("Past Orders").withSelectable(false).withTextColor(ContextCompat.getColor(context!!, R.color.colorPrimaryLight)),
                PrimaryDrawerItem().withIdentifier(2).withName("Stats").withSelectable(false).withTextColor(ContextCompat.getColor(context!!, R.color.colorPrimaryLight)),
                PrimaryDrawerItem().withIdentifier(3).withName("Profile").withSelectable(false).withTextColor(ContextCompat.getColor(context!!, R.color.colorPrimaryLight)),
                DividerDrawerItem(),
                SecondaryDrawerItem().withIdentifier(5).withName("Sign Out").withSelectable(false).withTextColor(ContextCompat.getColor(context!!, R.color.red))
            )
            .withOnDrawerItemClickListener(object: Drawer.OnDrawerItemClickListener{
                override fun onItemClick(view: View?, position: Int, drawerItem: IDrawerItem<*>): Boolean {
                    when(position){
                        0 -> {
                            val i = Intent(context, VendorInventoryActivity::class.java)
                            startActivity(i)
                        }
                        1-> {
                            val i = Intent(context, OrdersActivity::class.java)
                            i.putExtra(USER_TYPE, userType_VENDOR)
                            startActivity(i)
                        }
                        2-> {
                            Toast.makeText(context, "Coming Soon...", Toast.LENGTH_SHORT).show()
                        }
                        3 -> {
                            val i = Intent(context!!, ProfileActivity::class.java)
                            i.putExtra(USER_TYPE, userType_VENDOR)
                            startActivity(i)
                        }
                        5 -> {
                            sessionManager.logout()
                            val i = Intent(context!!, SplashActivity::class.java)
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            startActivity(i)
                        }
                    }
                    shopDrawer?.closeDrawer()
                    return true
                }
            })
            .build()
        shopDrawer?.setSelection(-1, false)
    }

    private fun initRecycler(){
        adapter = OrderAdapter(true)
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

    //    fun onBackPressed(): Boolean{
//        shopDrawer?.let {
//            if(it.isDrawerOpen){
//                it.closeDrawer()
//                return true
//            }
//        }
//        return false
//    }
    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.vendor_main_header_profile -> {
                shopDrawer?.openDrawer()
            }
        }
    }
}