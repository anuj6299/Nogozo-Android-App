package com.startup.startup.di.network.city

import com.startup.startup.datamodels.City
import com.startup.startup.datamodels.User
import io.reactivex.Flowable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CityApi {
    @GET("city")
    suspend fun authUser(): Flowable<List<City>>
}