package com.startup.startup.ui.main.vendor.orders.current

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.startup.startup.SessionManager
import com.startup.startup.datamodels.Order
import com.startup.startup.network.Database
import com.startup.startup.ui.main.DataResource
import com.startup.startup.ui.main.vendor.orders.OrderAdapter
import com.startup.startup.util.Constants.userType_VENDOR
import javax.inject.Inject

class VendorCurrentOrdersFragmentViewModel
    @Inject
    constructor(
        private val sessionManager: SessionManager,
        private val database: Database
    ): ViewModel() {

    private val currentOrders: MediatorLiveData<DataResource<ArrayList<Order>>> = MediatorLiveData()
    private val shopStatus: MediatorLiveData<String> = MediatorLiveData()
    private val temparrayList: ArrayList<Order> = ArrayList()

    fun getCurrentOrders(){
        if(currentOrders.value != null){
            if(currentOrders.value!!.status == DataResource.Status.LOADING)
                return
        }
        currentOrders.value = DataResource.loading()
        database.getCurrentOrders(sessionManager.getUserType(), sessionManager.getUserId()).addValueEventListener(object: ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                currentOrders.value = DataResource.error(error.message)
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.value != null){
                    val orderIds = snapshot.value as HashMap<String, String>
                    if(orderIds.size == 0){
                        currentOrders.value = DataResource.error("No Orders")
                    }else{
                        getOrderDetails(orderIds.keys)
                    }
                }else{
                    currentOrders.value = DataResource.error("No Orders")
                }
            }
        })
    }

    fun getLiveData(): LiveData<DataResource<ArrayList<Order>>>{
        return currentOrders
    }

    private fun getOrderDetails(orderId: Set<String>){
        for(key in orderId){
            database.getOrderDetails(key).addListenerForSingleValueEvent(object: ValueEventListener{
                override fun onCancelled(error: DatabaseError) {
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val order = snapshot.getValue<Order>()!!
                    order.orderId = snapshot.key!!
                    temparrayList.add(order)

                    if(temparrayList.size == orderId.size){
                        currentOrders.value = DataResource.success(temparrayList.clone() as ArrayList<Order>)
                        temparrayList.clear()
                    }
                }
            })
        }
    }

    fun getCurrentShopStatus(){
        database.getShopStatus(sessionManager.getUserId()).addValueEventListener(object: ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                shopStatus.value = snapshot.value as String
            }
        })
    }

    fun getStatusLiveData(): LiveData<String>{
        return shopStatus
    }

    fun changeShopStatus(status: String): Task<Void> {
        return database.changeShopStatus(sessionManager.getUserId(), status, userType_VENDOR)
    }
}