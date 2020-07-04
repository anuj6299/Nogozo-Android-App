package com.startup.startup.ui.inventory.inventory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.startup.startup.SessionManager
import com.startup.startup.datamodels.Item
import com.startup.startup.network.Database
import com.startup.startup.ui.main.DataResource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class VendorInventoryFragmentViewModel
@Inject
constructor(
    private val sessionManager: SessionManager,
    private val database: Database
): ViewModel() {

    private val itemsList: MediatorLiveData<DataResource<ArrayList<Item>>> = MediatorLiveData()

    fun getLiveData(): LiveData<DataResource<ArrayList<Item>>> {
        return itemsList
    }

    fun getItems(){
        if(itemsList.value != null){
            if(itemsList.value!!.status == DataResource.Status.LOADING){
                return
            }
        }

        itemsList.value = DataResource.loading()
        database.getItems(sessionManager.getUserId()).addListenerForSingleValueEvent(object:
            ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                itemsList.value = DataResource.error(error.message)
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.value == null)
                    itemsList.value = DataResource.error("No Items in Your Shop")
                else{
                    CoroutineScope(Dispatchers.Default).launch{
                        val items: ArrayList<Item> = ArrayList()
                        val data = snapshot.value as HashMap<String, Any>
                        for((key, value) in data){
                            val data2 = value as HashMap<String, String>
                            val item = Item(key, data2["itemname"], data2["itemprice"]!!, data2["quantity"], data2["itemimageurl"],data2["isAvailable"] == "true")
                            items.add(item)
                        }

                        itemsList.postValue(DataResource.success(items))
                    }
                }
            }
        })
    }

    fun changeItemStatus(itemId: String, itemStatus: String){
        database.changeItemAvailabilityStatus(sessionManager.getUserId(), itemId, itemStatus)
    }
}