package com.startup.startup.ui.main.customer.shops

import androidx.lifecycle.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.startup.startup.SessionManager
import com.startup.startup.datamodels.Shop
import com.startup.startup.network.Database
import com.startup.startup.ui.main.DataResource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class ShopListFragmentViewModel
    @Inject
    constructor(
        var sessionManager: SessionManager
    ): ViewModel() {

    private val shopList: MediatorLiveData<DataResource<ArrayList<Shop>>> = MediatorLiveData()

    fun getShopsList(serviceId: String): LiveData<DataResource<ArrayList<Shop>>> {

        shopList.value = DataResource.loading()

        Database().getShops(serviceId, sessionManager.getAreaId())
            .addListenerForSingleValueEvent(object: ValueEventListener{
                override fun onCancelled(error: DatabaseError) {
                    shopList.value = DataResource.error(error.message)
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    CoroutineScope(Dispatchers.Default).launch{
                        if(snapshot.value != null){
                            val map = snapshot.value as HashMap<String, String>
                            val shops: ArrayList<Shop> = ArrayList()
                            for((key, value) in map){
                                shops.add(Shop(value, key, ""))
                            }
                            shopList.postValue(DataResource.success(shops))
                        }else{
                            shopList.postValue(DataResource.error("No Shops in Your Area"))
                        }
                    }
                }
            })

        return shopList
    }
}