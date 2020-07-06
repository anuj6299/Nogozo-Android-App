package com.startup.startup

import android.content.SharedPreferences
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.startup.startup.datamodels.CustomerProfile
import com.startup.startup.datamodels.VendorProfile
import com.startup.startup.network.Auth
import com.startup.startup.network.Database
import com.startup.startup.ui.auth.AuthResource
import com.startup.startup.util.Constants.ADDRESS
import com.startup.startup.util.Constants.AREA_ID
import com.startup.startup.util.Constants.AREA_NAME
import com.startup.startup.util.Constants.CITY_ID
import com.startup.startup.util.Constants.CITY_NAME
import com.startup.startup.util.Constants.EMAIL
import com.startup.startup.util.Constants.NAME
import com.startup.startup.util.Constants.PHONE
import com.startup.startup.util.Constants.PROFILE_LEVEL
import com.startup.startup.util.Constants.PROFILE_LEVEL_0
import com.startup.startup.util.Constants.PROFILE_LEVEL_1
import com.startup.startup.util.Constants.USER_TYPE
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager
@Inject constructor(
    private val database: Database,
    private val preferences: SharedPreferences,
    private val editPreferences: SharedPreferences.Editor) {

    fun getCurrentUser(): AuthResource{
        val auth = FirebaseAuth.getInstance()
        if(auth.currentUser != null)
            return AuthResource.authenticated()
        return AuthResource.notAuthenticated()
    }

    fun getUserId(): String{
        return FirebaseAuth.getInstance().currentUser!!.uid
    }

     fun logout(){
        editPreferences.clear().apply()
        FirebaseAuth.getInstance().signOut()
    }

    fun getUserType(): String{
        return preferences.getString(USER_TYPE, "-1")!!
    }

    fun getAreaId(): String{
        return preferences.getString(AREA_ID,"-1")!!
    }

    fun getAreaName(): String{
        return preferences.getString(AREA_NAME, "")!!
    }

    fun getProfileLevel(): String{
        return preferences.getString(PROFILE_LEVEL, PROFILE_LEVEL_0)!!
    }

    fun login(email: String, password: String): Task<AuthResult> {
        return Auth().login(email, password)
    }

    fun register(email: String, password: String): Task<AuthResult> {
        return Auth().register(email, password)
    }

    fun getUserProfile(): DatabaseReference{
        return database.getUserProfile(getUserType())
    }

    fun getUserName(): String{
        return preferences.getString(NAME,"")!!
    }
    fun getUserPhone(): String{
        return preferences.getString(PHONE,"")!!
    }
    fun getUserAddress(): String{
        return preferences.getString(ADDRESS,"")!!
    }

    fun saveProfileToLocal(profile: CustomerProfile){
        CoroutineScope(Dispatchers.Default).launch {
            editPreferences.putString(PROFILE_LEVEL, PROFILE_LEVEL_1).apply()
            editPreferences.putString(EMAIL, profile.email).apply()
            editPreferences.putString(CITY_ID, profile.cityid).apply()
            editPreferences.putString(CITY_NAME, profile.cityname).apply()
            editPreferences.putString(AREA_ID, profile.areaid).apply()
            editPreferences.putString(AREA_NAME, profile.areaname).apply()
            editPreferences.putString(NAME, profile.name).apply()
            editPreferences.putString(ADDRESS, profile.address).apply()
            editPreferences.putString(PHONE, profile.phone).apply()
        }
    }

    fun saveCustomerProfileToLocal(map: HashMap<String, Any>){
        CoroutineScope(Dispatchers.Default).launch {
            editPreferences.putString(PROFILE_LEVEL, PROFILE_LEVEL_1).apply()
            editPreferences.putString(CITY_ID, map["cityid"] as String).apply()
            editPreferences.putString(CITY_NAME, map["cityname"] as String).apply()
            editPreferences.putString(AREA_ID, map["areaid"] as String).apply()
            editPreferences.putString(AREA_NAME, map["areaname"] as String).apply()
            editPreferences.putString(NAME, map["name"] as String).apply()
            editPreferences.putString(ADDRESS, map["address"] as String).apply()
            editPreferences.putString(PHONE, map["phone"] as String).apply()
        }
    }

    fun saveVendorProfileToLocal(map: HashMap<String, Any>){}

    fun saveVendorProfileToLocal(profile: VendorProfile){}

    fun saveOnRegistered(email: String, userType: String){
        CoroutineScope(Dispatchers.Default).launch{
            editPreferences.putString(EMAIL, email).apply()
            editPreferences.putString(PROFILE_LEVEL, PROFILE_LEVEL_0).apply()
            editPreferences.putString(USER_TYPE, userType).apply()
            database.setUserProfileOnRegistered(userType, CustomerProfile(email, PROFILE_LEVEL_0))
        }
    }

    fun uploadToken(token: String){
        database.uploadToken(token)
    }

    fun saveOnLogged(email: String, userType: String){
        editPreferences.putString(EMAIL, email).apply()
        editPreferences.putString(USER_TYPE, userType).apply()
    }
}