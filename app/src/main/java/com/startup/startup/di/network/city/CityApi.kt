package com.startup.startup.di.network.city

import com.startup.startup.datamodels.City
import io.reactivex.Flowable
import retrofit2.http.GET

interface CityApi {
    @GET("city")
    fun getCitiesList(): Flowable<List<City>>
}