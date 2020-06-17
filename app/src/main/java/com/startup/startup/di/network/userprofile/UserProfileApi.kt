package com.startup.startup.di.network.userprofile

import com.startup.startup.datamodels.User
import io.reactivex.Flowable
import retrofit2.http.Field
import retrofit2.http.GET

interface UserProfileApi {
    @GET("updateprofile")
    fun updateProfile(
        @Field("cityid") cityId: String,
        @Field("areaid") areaId: String,
        @Field("address") address: String,
        @Field("lat") lat: Double,
        @Field("lon") lon: Double
    ): Flowable<User>

}