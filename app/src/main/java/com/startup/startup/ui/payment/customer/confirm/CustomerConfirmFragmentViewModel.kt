package com.startup.startup.ui.payment.customer.confirm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.startup.startup.SessionManager
import com.startup.startup.network.Database
import com.startup.startup.ui.main.DataResource
import com.startup.startup.util.Constants.AREA_ID
import com.startup.startup.util.Constants.CITY_ID
import javax.inject.Inject

class CustomerConfirmFragmentViewModel
@Inject
constructor(
    private val sessionManager: SessionManager,
    private val database: Database
) : ViewModel() {

    val extraFare: MediatorLiveData<DataResource<HashMap<String, String>>> = MediatorLiveData()

    fun getUserAddress(): String {
        return sessionManager.getUserAddress()
    }

    fun getUserPhone(): String {
        return sessionManager.getUserPhone()
    }

    fun getUserName(): String {
        return sessionManager.getUserName()
    }

    fun getUserId(): String {
        return FirebaseAuth.getInstance().currentUser!!.uid
    }

    fun getFareLiveData(): LiveData<DataResource<HashMap<String, String>>> {
        return extraFare
    }

    fun getFare(price: String, shopAreaId: String) {
        extraFare.value = DataResource.loading();

        database.getFare(price, sessionManager.currentSessionData[CITY_ID]!!,sessionManager.currentSessionData[AREA_ID]!!, shopAreaId)
            .continueWith {
                if (it.isSuccessful) {
                    val data = it.result!!.data as HashMap<String, String>
                    if (data.isNullOrEmpty())
                        extraFare.value = DataResource.error("Something went wrong")
                    else
                        extraFare.value = DataResource.success(data)
                } else {
                    extraFare.value = DataResource.error(it.exception!!.localizedMessage!!)
                }
            }
    }
}