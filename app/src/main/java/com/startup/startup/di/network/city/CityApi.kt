package com.startup.startup.di.network.city

import com.startup.startup.datamodels.City
import com.startup.startup.datamodels.ListOfArea
import io.reactivex.Flowable
import retrofit2.http.GET
import retrofit2.http.Path

interface CityApi {
    @GET("city")
    fun getCitiesList(): Flowable<List<City>>

    @GET("area/{areaid}")
    fun getAreaOfCity(
        @Path("areaid") areaid: String
    ): Flowable<ListOfArea>
}