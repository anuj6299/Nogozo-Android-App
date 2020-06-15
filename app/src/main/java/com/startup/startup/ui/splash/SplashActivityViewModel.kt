package com.startup.startup.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.startup.startup.SessionManager
import com.startup.startup.datamodels.User
import com.startup.startup.ui.auth.AuthResource
import javax.inject.Inject

class SplashActivityViewModel
    @Inject
    constructor(
        private val sessionManager: SessionManager
    ): ViewModel() {

    fun getCurrentUser(): LiveData<AuthResource<User>> {
        return sessionManager.getCurrentUser()
    }

}