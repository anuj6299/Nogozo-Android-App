package com.startup.startup.ui.orders.customer.current

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
import com.startup.startup.util.Constants.userType_CUSTOMER
import javax.inject.Inject

class CustomerCurrentOrdersFragmentViewModel
    @Inject
    constructor(
        private val sessionManager: SessionManager
    ): ViewModel() {

    private var currentOrders: MediatorLiveData<DataResource<ArrayList<Order>>> = MediatorLiveData()
    private var temparrayList: ArrayList<Order> = ArrayList()

    fun getLiveData(): LiveData<DataResource<ArrayList<Order>>> {
        return currentOrders
    }

    fun getCurrentOrderOrders(){
        if(currentOrders.value != null){
            if(currentOrders.value!!.status == DataResource.Status.LOADING){
                return
            }
        }
        currentOrders.value = DataResource.loading()
        Database().getCurrentOrders(userType_CUSTOMER, sessionManager.getUserId())
            .addListenerForSingleValueEvent(object: ValueEventListener{
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

    private fun getOrderDetails(orderId: Set<String>){
        for(key in orderId){
            Database().getOrderDetails(key).addListenerForSingleValueEvent(object: ValueEventListener{
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
}