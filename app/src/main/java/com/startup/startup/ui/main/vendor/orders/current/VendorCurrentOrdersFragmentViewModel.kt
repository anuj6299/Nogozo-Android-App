package com.startup.startup.ui.main.vendor.orders.current

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.startup.startup.SessionManager
import com.startup.startup.datamodels.Order
import com.startup.startup.network.Database
import com.startup.startup.ui.main.DataResource
import com.startup.startup.ui.main.vendor.orders.OrderAdapter
import javax.inject.Inject

class VendorCurrentOrdersFragmentViewModel
    @Inject
    constructor(
        val sessionManager: SessionManager
    ): ViewModel() {

    private val currentOrders: MediatorLiveData<DataResource<ArrayList<Order>>> = MediatorLiveData()

    fun getCurrentOrders(){
        if(currentOrders.value != null){
            if(currentOrders.value!!.status == DataResource.Status.LOADING)
                return
        }
        currentOrders.value = DataResource.loading()
        Database().getCurrentOrders(sessionManager.getUserType(), sessionManager.getUserId()).addValueEventListener(object: ValueEventListener{
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
            Database().getOrderDetails(key).addListenerForSingleValueEvent(object: ValueEventListener{
                override fun onCancelled(error: DatabaseError) {
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val order = snapshot.getValue<Order>()!!
                    order.orderId = snapshot.key!!
                    if(currentOrders.value!!.data.isNullOrEmpty()){
                        val arrayList: ArrayList<Order> = ArrayList()
                        arrayList.add(order)
                        currentOrders.value = DataResource.success(arrayList)
                    }else{
                        val list = currentOrders.value!!.data
                        list.add(order)
                        currentOrders.value = DataResource.success(list)
                    }
                }
            })
        }
    }

}