package com.startup.startup.ui.main.customer.shops

import androidx.lifecycle.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.startup.startup.SessionManager
import com.startup.startup.datamodels.Shop
import com.startup.startup.network.Database
import com.startup.startup.ui.main.DataResource
import com.startup.startup.util.Constants.CITY_ID
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class ShopListFragmentViewModel
    @Inject
    constructor(
        private val sessionManager: SessionManager,
        private val database: Database
    ): ViewModel() {

    private val shopList: MediatorLiveData<DataResource<ArrayList<Shop>>> = MediatorLiveData()

    fun getShopsList(serviceId: String) {

        if(shopList.value != null){
            if(shopList.value!!.status == DataResource.Status.LOADING){
                return
            }
        }

        shopList.value = DataResource.loading()

        database.getShops(serviceId, sessionManager.currentSessionData[CITY_ID]!!)
            .addListenerForSingleValueEvent(object: ValueEventListener{
                override fun onCancelled(error: DatabaseError) {
                    shopList.value = DataResource.error(error.message)
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    CoroutineScope(Dispatchers.Default).launch{
                        if(snapshot.value != null){
                            val map = snapshot.value as HashMap<String, Any>
                            val shops: ArrayList<Shop> = ArrayList()
                            for((key, value) in map){
                                val shop = value as HashMap<String, String>
                                shops.add(Shop(shop["shopname"]!!, key, shop["imageurl"], null, shop["areaid"]!!))
                            }
                            shopList.postValue(DataResource.success(shops))
                        }else{
                            shopList.postValue(DataResource.error("No Shops in Your Area"))
                        }
                    }
                }
            })
    }

    fun getShopLiveData(): LiveData<DataResource<ArrayList<Shop>>>{
        return shopList
    }
}