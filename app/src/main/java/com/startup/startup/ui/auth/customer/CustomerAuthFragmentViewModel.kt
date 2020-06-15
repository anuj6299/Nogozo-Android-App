package com.startup.startup.ui.auth.customer

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.startup.startup.SessionManager
import com.startup.startup.datamodels.User
import com.startup.startup.ui.auth.AuthResource
import com.startup.startup.util.Constants.userType_CUSTOMER
import javax.inject.Inject

class CustomerAuthFragmentViewModel
@Inject
constructor(
    private val sessionManager: SessionManager
): ViewModel() {

    fun authenticateUser(userId: String, password: String, userType: String){
        sessionManager.authenticateUser(userId, password, userType)
    }

    fun registerUser(name: String, userId: String, password: String){
        sessionManager.registerUser(name, userId, password, userType_CUSTOMER)
    }

    fun getCachedUser(): LiveData<AuthResource<User>> {
        return sessionManager.getCurrentUser()
    }
}