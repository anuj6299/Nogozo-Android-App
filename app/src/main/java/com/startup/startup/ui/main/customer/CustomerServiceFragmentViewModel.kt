package com.startup.startup.ui.main.customer

import androidx.lifecycle.*
import com.startup.startup.SessionManager
import com.startup.startup.datamodels.Services
import com.startup.startup.di.network.main.ServicesApi
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class CustomerServiceFragmentViewModel
@Inject
constructor(
    val sessionManager: SessionManager//,
    //val servicesApi: ServicesApi
): ViewModel(){

    private val services: MediatorLiveData<ServiceResource<List<Services>>> = MediatorLiveData()

    fun getServices(/*cityId: String, areaId: String*/): LiveData<ServiceResource<List<Services>>> {

        services.value = ServiceResource.loading()

//        val source = LiveDataReactiveStreams.fromPublisher(
//            servicesApi.getServices()
//                .onErrorReturn{
//                    val errorService = Services(it.message!!,"-1","-1")
//                    val errorServices:ArrayList<Services> = ArrayList()
//                    errorServices.add(errorService)
//                    errorServices
//                }
//                .map {
//                    if(it.isNotEmpty()){
//                        if (it[0].serviceId == "-1")
//                            ServiceResource.error<Services>(it[0].serviceName)
//                    }
//                    ServiceResource.success(it)
//                }
//                .subscribeOn(Schedulers.io())
//        )
//
//        services.addSource(source, Observer {
//            services.value = it
//            services.removeSource(source)
//        })

        val list: ArrayList<Services> = ArrayList()
        list.add(Services("service1","serviceid1","url1"))
        list.add(Services("service2","serviceid2","url2"))
        list.add(Services("service3","serviceid3","url3"))
        list.add(Services("service4","serviceid4","url4"))
        services.value = ServiceResource.success(list)

        return services
    }
}