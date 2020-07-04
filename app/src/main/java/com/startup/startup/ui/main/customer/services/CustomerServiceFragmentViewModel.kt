package com.startup.startup.ui.main.customer.services

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.startup.startup.SessionManager
import com.startup.startup.datamodels.Services
import com.startup.startup.network.Database
import com.startup.startup.ui.main.DataResource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject

class CustomerServiceFragmentViewModel
@Inject
constructor(
    val sessionManager: SessionManager
): ViewModel(){

    private val services: MediatorLiveData<DataResource<List<Services>>> = MediatorLiveData()
    private var isLoading: Boolean = false

    fun getLiveData(): LiveData<DataResource<List<Services>>>{
        return services
    }

    fun getServices(){

        if(isLoading)
            return

        services.value = DataResource.loading()

        Database().getServices().addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                services.value = DataResource.error(error.message)
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                CoroutineScope(IO).launch{
                    val list: ArrayList<Services> = ArrayList()
                    val service = snapshot.value as HashMap<String, Any>
                    for((key, value) in service){
                        val a = value as HashMap<String, String>
                        list.add(Services(key, a["servicename"]!!, a["imageurl"]!!))
                    }
                    services.postValue(DataResource.success(list))
                }
            }
        })
    }
}