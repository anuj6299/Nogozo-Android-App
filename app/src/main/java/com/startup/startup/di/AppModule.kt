package com.startup.startup.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.startup.startup.network.Database
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class AppModule {
    @Provides
    @Singleton
    fun provideContext(app: Application): Context {
        return app.applicationContext
    }

//    @Singleton
//    @Provides
//    fun provideRetrofitInstance(): Retrofit {
//        return Retrofit.Builder()
//            .baseUrl(Constants.BASE_URL)
//            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//    }

//    @Singleton
//    @Provides
//    fun provideRequestOptions(): RequestOptions {
//        return RequestOptions
//            .placeholderOf(R.drawable.ic_launcher_background)
//            .error(R.drawable.ic_location)
//    }
    @Provides
    @Singleton
    fun provideSharedPreferences(application: Application): SharedPreferences {
        return application.getSharedPreferences("preference", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideSharedPreferencesEditor(application: Application): SharedPreferences.Editor{
        return application.getSharedPreferences("preference", Context.MODE_PRIVATE).edit()
    }

    @Provides
    @Singleton
    fun provideDatabase(): Database{
        return Database()
    }

//    @Provides
//    @Singleton
//    fun provideOkHttpClient(): OkHttpClient{
//        return
//    }
}