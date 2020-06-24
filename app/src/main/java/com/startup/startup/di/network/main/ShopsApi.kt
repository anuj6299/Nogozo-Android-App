package com.startup.startup.di.network.main

import com.startup.startup.datamodels.Shop
import io.reactivex.Flowable
import retrofit2.http.GET
import retrofit2.http.Path

interface ShopsApi {

    @GET("/shops/{service}/{area}")
    fun getShopsList(
        @Path("service") service: String,
        @Path("area") area: String
    ): Flowable<List<Shop>>
}