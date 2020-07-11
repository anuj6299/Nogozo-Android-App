package com.startup.startup.ui.main.customer.services

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.startup.startup.SessionManager
import com.startup.startup.datamodels.Area
import com.startup.startup.datamodels.Services
import com.startup.startup.network.Database
import com.startup.startup.ui.main.DataResource
import com.startup.startup.ui.userdetails.CityResource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject

class CustomerServiceFragmentViewModel
@Inject
constructor(
    val sessionManager: SessionManager
): ViewModel(){

    private val services: MediatorLiveData<DataResource<List<Services>>> = MediatorLiveData()
    private var areas: MediatorLiveData<CityResource<ArrayList<Area>>> = MediatorLiveData()

    fun getLiveData(): LiveData<DataResource<List<Services>>>{
        return services
    }

    fun getServices(){

        if(services.value != null){
            if(services.value!!.status == DataResource.Status.LOADING)
                return
        }

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
                        list.add(Services(key, a["servicename"]!!, a["imageurl"]))
                    }
                    services.postValue(DataResource.success(list))
                }
            }
        })
    }

    fun getAreaLiveData(): LiveData<CityResource<ArrayList<Area>>>{
        return areas
    }

    fun getAreaOfCity(cityId: String){
        areas.value = CityResource.loading()

        Database().getAreas(cityId).addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                areas.value = CityResource.error(error.message)
            }
            override fun onDataChange(snapshot: DataSnapshot) {
                CoroutineScope(Dispatchers.Default).launch{
                    val list: ArrayList<Area> = ArrayList()
                    val map = snapshot.value as HashMap<String, String>
                    for((key, value) in map){
                        list.add(Area(value, key))
                    }
                    areas.postValue(CityResource.success(list))
                }
            }
        })
    }
}