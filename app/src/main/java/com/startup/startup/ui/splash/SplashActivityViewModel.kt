package com.startup.startup.ui.splash

import androidx.lifecycle.ViewModel
import com.startup.startup.SessionManager
import com.startup.startup.ui.auth.AuthResource
import javax.inject.Inject

class SplashActivityViewModel
    @Inject
    constructor(
        private val sessionManager: SessionManager
    ): ViewModel() {

    suspend fun getCurrentUser(): AuthResource {
        return sessionManager.getCurrentUser()
    }

    suspend fun getUserType(): String {
        return sessionManager.getUserType()
    }

    suspend fun getProfileLevel(): String {
        return sessionManager.getProfileLevel()
    }
}