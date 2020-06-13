package com.startup.startup

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.startup.startup.datamodels.User
import com.startup.startup.util.AuthResource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager
@Inject constructor() {

    @Inject
    lateinit var preference: SharedPreferences

    @Inject
    lateinit var editPreferences: SharedPreferences.Editor

    private var cachesdUser: MediatorLiveData<AuthResource<User>> = MediatorLiveData()

    fun authenticateUser(userid: String){
        //Fake Auth
        //TODO PRIORITY HIGH
        //call retrofit get method
    }

    fun registerUser(){
        //Fake Auth
        //TODO PRIORITY HIGH
        //call retrofit get method

        //authenticate(userid)
    }

    /**
     *
     * COMPLETED
     *
     * */
    fun getCurrentUser(): LiveData<AuthResource<User>>{

        cachesdUser.value = AuthResource.loading()

        //Coroutine to get sharedPreferences
        CoroutineScope(IO).launch {
            val logged: Boolean = preference.getBoolean("logged",false)
            if(logged){
                val user:User = User(preference.getString("userid","")!!,preference.getString("usertype","")!!,preference.getString("name","")!!)
                cachesdUser.postValue(AuthResource.authenticated(user))
            }else {
                val user:User = User(preference.getString("userid","")!!,preference.getString("usertype","")!!,preference.getString("name","")!!)
                cachesdUser.postValue(AuthResource.notAuthenticated(user))
            }
        }

        return cachesdUser
    }

    /**
     *
     * COMPLETED
     *
     * */
    fun logout(){
        editPreferences.clear().apply()
        val user:User = User(preference.getString("userid","")!!,preference.getString("usertype","")!!,preference.getString("name","")!!)
        cachesdUser.value = AuthResource.notAuthenticated(user)
    }

}