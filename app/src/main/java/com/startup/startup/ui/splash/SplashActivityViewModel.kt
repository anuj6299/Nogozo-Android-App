package com.startup.startup.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.startup.startup.SessionManager
import com.startup.startup.datamodels.User
import com.startup.startup.util.AuthResource
import javax.inject.Inject

class SplashActivityViewModel
    @Inject
    constructor(
        val sessionManager: SessionManager
    ): ViewModel() {

    init {
        println("THIS IS VIEW MODEL")
    }

    fun getCurrentUser(): LiveData<AuthResource<User>> {
        return sessionManager.getCurrentUser()
    }

}