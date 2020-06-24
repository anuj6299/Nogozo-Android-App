package com.startup.startup.ui.auth.vendor

import androidx.lifecycle.ViewModel
import com.startup.startup.SessionManager
import javax.inject.Inject

class VendorAuthFragmentViewModel
@Inject constructor(
    private val sessionManager: SessionManager
):ViewModel() {

//    fun authenticateUser(userId: String, password: String, userType: String) {
//        sessionManager.authenticateUser(userId, password, userType)
//    }
//
//    fun getCachedUser(): LiveData<AuthResource<User>>{
//        return sessionManager.getCurrentUser()
//    }
}