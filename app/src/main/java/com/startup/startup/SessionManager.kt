package com.startup.startup

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.startup.startup.datamodels.User
import com.startup.startup.ui.auth.AuthResource
import com.startup.startup.util.Constants.LOADING
import com.startup.startup.util.Constants.NAME
import com.startup.startup.util.Constants.PROFILE_LEVEL
import com.startup.startup.util.Constants.PROFILE_LEVEL_0
import com.startup.startup.util.Constants.PROFILE_LEVEL_1
import com.startup.startup.util.Constants.SUCCESS
import com.startup.startup.util.Constants.USER_ID
import com.startup.startup.util.Constants.USER_TYPE
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager
@Inject constructor(val preferences: SharedPreferences, val editPreferences: SharedPreferences.Editor) {

    private var cachedUser: MediatorLiveData<AuthResource<User>> = MediatorLiveData()
    private var requestStatus: MediatorLiveData<String> = MediatorLiveData()

    init {
        cachedUser.value = AuthResource.loading()

        CoroutineScope(IO).launch {
            val logged: Boolean = preferences.getBoolean("logged",false)
            if(logged){
                val user = User(
                    preferences.getString(USER_ID,"")!!,
                    preferences.getString(USER_TYPE,"")!!,
                    preferences.getString(NAME,"")!!,
                    preferences.getString(PROFILE_LEVEL,"")!!
                )
                cachedUser.postValue(AuthResource.authenticated(user))
            }else {
                cachedUser.postValue(AuthResource.notAuthenticated())
            }
        }
    }

    fun authenticateUser(userid: String, password: String, userType: String){
        cachedUser.value = AuthResource.loading()
        //Fake Auth
        //TODO PRIORITY HIGH
        //call retrofit get method
        //put int shared preference
        //
        //***********WILL RETURN (USERTYPE,NAME,PrefferedCity) ON SUCCESS***********
        CoroutineScope(IO).launch {
            //editPreferences.putString("name",name).apply()
            //editPreferences.putString("prefferedcity",prefferedCity).apply()
            //cachedUser.postValue(AuthResource.authenticated(User(userid,userType,name)))
            val user = fakeLogin(userid, userType, password)
            if(user.userId == "-1" || userType == "-1" || user.name == "-1" || user.profileLevel == "-1"){
                cachedUser.postValue(AuthResource.notAuthenticated())
            }else{
                editPreferences.putBoolean("logged", true).apply()
                editPreferences.putString(USER_ID, user.userId).apply()
                editPreferences.putString(USER_TYPE, user.userType).apply()
                editPreferences.putString(PROFILE_LEVEL, user.profileLevel).apply()
                cachedUser.postValue(AuthResource.authenticated(user))
            }
        }
    }

    private fun fakeLogin(userid: String, userType: String, password: String): User{
        if(userid == "1" && password == "password"){
            return User(userid,userType,"fake name", PROFILE_LEVEL_0)
        }else{
            return User("-1","-1","-1","-1")
        }
    }

    fun registerUser(name: String, userid: String, password: String, userType: String){
        //Fake Auth
        //TODO PRIORITY HIGH
        //call retrofit post method

        //authenticate(userid)
        CoroutineScope(IO).launch{

        }
    }

    fun updateUserProfile(cityId: String, areaId: String, address: String, lat: Double, lon: Double): LiveData<String>{
        requestStatus.value = LOADING
        CoroutineScope(IO).launch{
            //make retrofit request
            //TODO PRIORITY HIGH
            editPreferences.putString(PROFILE_LEVEL, PROFILE_LEVEL_1).apply()
            val user = cachedUser.value!!.data
            user.profileLevel = PROFILE_LEVEL_1
            cachedUser.postValue(AuthResource.authenticated(user))
            requestStatus.postValue(SUCCESS)//FAKE
        }
        return requestStatus
    }

    /**
     *
     * COMPLETED
     *
     * */
    fun getCurrentUser(): LiveData<AuthResource<User>>{
        return cachedUser
    }

    /**
     *
     * COMPLETED
     *
     * */
    fun logout(){
        editPreferences.clear().apply()
        cachedUser.value = AuthResource.notAuthenticated()
    }

}