package com.startup.startup.di.userdetails

import com.startup.startup.di.network.city.CityApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class UserDetailsModule {

    @Provides
    @UserDetailsScope
    fun provideCityApi(retrofit: Retrofit): CityApi{
        return retrofit.create(CityApi::class.java)
    }
}