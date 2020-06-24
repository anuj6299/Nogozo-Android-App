package com.startup.startup.ui.main.customer.services

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.startup.startup.SessionManager
import com.startup.startup.datamodels.Services
import com.startup.startup.di.network.main.ServicesApi
import com.startup.startup.network.Database
import com.startup.startup.ui.main.DataResource
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject

class CustomerServiceFragmentViewModel
@Inject
constructor(
    val sessionManager: SessionManager,
    val servicesApi: ServicesApi
): ViewModel(){

    private val services: MediatorLiveData<DataResource<List<Services>>> = MediatorLiveData()

    fun getServices(): LiveData<DataResource<List<Services>>> {

        services.value = DataResource.loading()

        Database().getServices().addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                services.value = DataResource.error(error.message)
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                CoroutineScope(IO).launch{
                    val list: ArrayList<Services> = ArrayList()
                    val map = snapshot.value as HashMap<String, String>
                    for((key, value) in map){
                        list.add(Services(key, value, ""))
                    }
                    services.postValue(DataResource.success(list))
                }
            }
        })

        return services
    }
}