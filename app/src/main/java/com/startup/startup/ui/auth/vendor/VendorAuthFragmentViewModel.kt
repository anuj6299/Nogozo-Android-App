package com.startup.startup.ui.auth.vendor

import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.database.DatabaseReference
import com.startup.startup.SessionManager
import com.startup.startup.datamodels.CustomerProfile
import com.startup.startup.util.Constants.userType_VENDOR
import javax.inject.Inject

class VendorAuthFragmentViewModel
@Inject constructor(
    private val sessionManager: SessionManager
):ViewModel() {

    suspend fun login(email: String, password: String): Task<AuthResult> {
        return sessionManager.login(email, password)
    }

    fun getUserProfile(): DatabaseReference {
        return sessionManager.getUserProfile()
    }

    fun saveProfileToLocal(profile: CustomerProfile){
        sessionManager.saveCustomerProfileToLocal(profile)
    }

    fun saveOnLogged(email: String){
        sessionManager.saveOnLogged(email, userType_VENDOR)
    }
}