package com.startup.startup.ui.userdetails.customer

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.startup.startup.SessionManager
import com.startup.startup.datamodels.City
import com.startup.startup.datamodels.User
import com.startup.startup.di.network.city.CityApi
import com.startup.startup.ui.auth.AuthResource
import com.startup.startup.ui.userdetails.CityResource
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class CustomerDetailsFragmentViewModel
@Inject
constructor(
    private val sessionManager: SessionManager,
    private val cityApi: CityApi
): ViewModel() {

    private var cities: MediatorLiveData<CityResource<List<City>>> = MediatorLiveData()

    fun getCurrentUser(): LiveData<AuthResource<User>>{
        return sessionManager.getCurrentUser()
    }

    fun logOut(){
        sessionManager.logout()
    }

    fun getCities(): LiveData<CityResource<List<User>>>{
        cities.value = CityResource.loading()

        val source: LiveData<CityResource<List<City>>> = LiveDataReactiveStreams.fromPublisher(
            cityApi.getCitiesList().onErrorReturn{
                val errorCity = City(it.message!!,"-1")
                val errorCities:ArrayList<City> = ArrayList()
                errorCities.add(errorCity)
                errorCities
            }
                .map{
                    if(it.isNotEmpty()){
                        if(it[0].cityId == "-1")
                            CityResource.error<City>(it[0].cityName)
                    }
                    CityResource.success(it)
                }
                .subscribeOn(Schedulers.io())
            )

        cities.addSource(source, Observer{
            cities.value = it
            cities.removeSource(source)
        })

        return cities
    }
}