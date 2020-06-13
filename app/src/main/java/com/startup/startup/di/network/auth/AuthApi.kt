package com.startup.startup.di.network.auth

import com.startup.startup.datamodels.User
import io.reactivex.Flowable
import retrofit2.http.*

interface AuthApi {
    @GET("users/{userid}")
    suspend fun getUser(
        @Path("userid") userid: String
    ): Flowable<User>

    @FormUrlEncoded
    @POST("users/{userid}")
    suspend fun createUser(
        @Field("userid") userid: String,
        @Field("name") name: String,
        @Field("usertype") usertype: String
        //@Field("name") name: String
    ): Flowable<User>
}