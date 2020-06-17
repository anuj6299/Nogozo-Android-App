package com.startup.startup.ui.userdetails.customer

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.startup.startup.SessionManager
import com.startup.startup.datamodels.Area
import com.startup.startup.datamodels.City
import com.startup.startup.datamodels.ListOfArea
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
    private var areas: MediatorLiveData<CityResource<ListOfArea>> = MediatorLiveData()

    fun getCurrentUser(): LiveData<AuthResource<User>>{
        return sessionManager.getCurrentUser()
    }

    fun logOut(){
        sessionManager.logout()
    }

    fun getCities(): LiveData<CityResource<List<City>>>{
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
        val fakeList: ArrayList<City> = ArrayList()
        fakeList.add(City("Fake Delhi","1"))
        fakeList.add(City("Fake Mumbai","2"))
        fakeList.add(City("Fake Agra","3"))
        fakeList.add(City("Fake Gwalior","4"))
        //cities.value = CityResource.success(fakeList)

        return cities
    }
    fun getAreaOfCity(cityId: String): LiveData<CityResource<ListOfArea>>{
        areas.value = CityResource.loading()

        val source: LiveData<CityResource<ListOfArea>> = LiveDataReactiveStreams.fromPublisher(
            cityApi.getAreaOfCity(cityId).onErrorReturn{ it ->
                val errorArea = Area(it.message!!, "-1")
                val errorAreas: ListOfArea = ListOfArea(cityId,List(1,init = {errorArea}))
                errorAreas
            }
                .map {
                    if(it.areas.isNotEmpty()){
                        if(it.areas[0].areaId == "-1")
                            CityResource.error<City>(it.areas[0].areaName)
                    }
                    CityResource.success(it)
                }
                .subscribeOn(Schedulers.io())
        )

        areas.addSource(source, Observer {
            areas.value = it
            areas.removeSource(source)
        })
        return areas
    }

    fun updateUserProfile(cityId: String, areaId: String, address: String, lat: Double, lon: Double): LiveData<String>{
        return sessionManager.updateUserProfile(cityId, areaId, address, lat, lon)
    }
}