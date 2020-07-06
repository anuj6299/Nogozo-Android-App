package com.startup.startup.ui.profile.customer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.startup.startup.SessionManager
import com.startup.startup.datamodels.Area
import com.startup.startup.datamodels.City
import com.startup.startup.datamodels.CustomerProfile
import com.startup.startup.network.Database
import com.startup.startup.ui.main.DataResource
import com.startup.startup.ui.userdetails.CityResource
import com.startup.startup.util.Constants.userType_CUSTOMER
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class CustomerProfileFragmentViewModel
    @Inject
    constructor(
        private val sessionManager: SessionManager,
        private val database: Database
    ): ViewModel() {

    private var cities: MediatorLiveData<CityResource<List<City>>> = MediatorLiveData()
    private var areas: MediatorLiveData<CityResource<List<Area>>> = MediatorLiveData()
    private var userProfile: MediatorLiveData<DataResource<CustomerProfile>> = MediatorLiveData()

    fun getCities(): LiveData<CityResource<List<City>>> {
        cities.value = CityResource.loading()

        database.getCities().addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                cities.value = CityResource.error(error.message)
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                CoroutineScope(Dispatchers.Default).launch{
                    val list: ArrayList<City> = ArrayList()
                    val map = snapshot.value as HashMap<String, String>
                    for((key, value) in map){
                        list.add(City(value, key))
                    }
                    cities.postValue(CityResource.success(list))
                }
            }
        })
        return cities
    }

    fun getAreaOfCity(cityId: String): LiveData<CityResource<List<Area>>> {
        areas.value = CityResource.loading()

        database.getAreas(cityId).addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                areas.value = CityResource.error(error.message)
            }
            override fun onDataChange(snapshot: DataSnapshot) {
                println(snapshot)
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

        return areas
    }

    fun getUserProfile(): LiveData<DataResource<CustomerProfile>>{
        userProfile.value = DataResource.loading()
        database.getUserProfile(userType_CUSTOMER).addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.value != null){
                    val profile = snapshot.getValue<CustomerProfile>()
                    userProfile.value = DataResource.success(profile!!)
                }else{
                    userProfile.value = DataResource.error("SomethingWentWrong")
                }
            }
        })
        return userProfile
    }

    fun updateUserProfile(map: HashMap<String, Any>): Task<Void> {
        return database.setUserProfile(sessionManager.getUserType() ,map)
    }

    fun saveProfileToLocal(map: HashMap<String, Any>){
        sessionManager.saveCustomerProfileToLocal(map)
    }
}