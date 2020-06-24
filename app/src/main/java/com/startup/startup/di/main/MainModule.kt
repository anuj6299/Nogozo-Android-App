package com.startup.startup.di.main

import com.startup.startup.di.network.main.ShopsApi
import com.startup.startup.di.network.main.ServicesApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class MainModule {

    @Provides
    fun provideShopApi(retrofit: Retrofit): ShopsApi{
        return retrofit.create(ShopsApi::class.java)
    }

    @Provides
    fun provideServicesApi(retrofit: Retrofit): ServicesApi{
        return retrofit.create(ServicesApi::class.java)
    }
}