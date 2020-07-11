package com.startup.startup.ui.profile.vendor

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.startup.startup.SessionManager
import com.startup.startup.datamodels.VendorProfile
import com.startup.startup.network.Database
import com.startup.startup.ui.main.DataResource
import com.startup.startup.util.Constants
import javax.inject.Inject

class VendorProfileFragmentViewModel
    @Inject
    constructor(
        private val sessionManager: SessionManager,
        private val database: Database
    ): ViewModel() {

    private var userProfile: MediatorLiveData<DataResource<VendorProfile>> = MediatorLiveData()

    fun getUserProfile(){
        if(userProfile.value != null){
            if(userProfile.value!!.status == DataResource.Status.LOADING)
                return
        }

        userProfile.value = DataResource.loading()
        database.getUserProfile(Constants.userType_VENDOR).addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                userProfile.value = DataResource.error(error.message)
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                println(snapshot)
                if(snapshot.value != null){
                    val profile = snapshot.getValue<VendorProfile>()
                    userProfile.value = DataResource.success(profile!!)
                }else{
                    userProfile.value = DataResource.error("Something Went Wrong")
                }
            }
        })
    }

    fun getLiveData():  LiveData<DataResource<VendorProfile>>{
        return userProfile
    }
}