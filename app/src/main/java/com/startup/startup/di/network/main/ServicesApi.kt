package com.startup.startup.di.network.main

import com.startup.startup.datamodels.Services
import io.reactivex.Flowable
import retrofit2.http.GET

interface ServicesApi {

    @GET
    fun getServices(): Flowable<List<Services>>
}